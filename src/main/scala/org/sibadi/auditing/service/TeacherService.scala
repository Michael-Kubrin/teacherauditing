package org.sibadi.auditing.service

import cats.data.{EitherT, OptionT}
import cats.effect.{MonadCancel, Resource}
import cats.syntax.all._
import org.sibadi.auditing.db
import org.sibadi.auditing.db._
import org.sibadi.auditing.domain._
import org.sibadi.auditing.domain.errors.AppError
import org.sibadi.auditing.util.{HashGenerator, PasswordGenerator, TokenGenerator}

import java.util.UUID

class TeacherService[F[_]](
  tokenGenerator: TokenGenerator[F],
  teacherDAO: TeacherDAO[F],
  teacherCredsDAO: TeacherCredentialsDAO[F],
  reviewerCredsDAO: ReviewerCredentialsDAO[F],
  teacherGroupDAO: TeacherGroupDAO[F],
  groupDAO: GroupDAO[F],
  hashGenerator: HashGenerator[F]
)(implicit M: MonadCancel[F, Throwable]) {

  def createTeacher(
    firstName: String,
    lastName: String,
    middleName: Option[String],
    login: String
  ): EitherT[F, AppError, CreatedTeacher] =
    for {
      isLoginExists <- EitherT.liftF(isLoginExists(login))
      _             <- EitherT.cond(!isLoginExists, (), AppError.LoginExists(login))
      teacherId     <- EitherT.pure(UUID.randomUUID().toString)
      _ <- EitherT(
        teacherDAO
          .createTeacher(db.Teacher(teacherId, firstName, lastName, middleName, none))
          .map(_.asRight[AppError])
          .handleError(throwable => AppError.Unexpected(throwable).asLeft[Unit])
      )
      password = PasswordGenerator.randomPassword(10)
      hash   <- EitherT.liftF(hashGenerator.hashPassword(password))
      bearer <- EitherT.liftF(tokenGenerator.generate)
      _      <- EitherT.liftF(teacherCredsDAO.insertCredentials(db.TeacherCredentials(teacherId, login, hash, bearer)))
    } yield CreatedTeacher(id = teacherId, password = password)

  private def isLoginExists(login: String): F[Boolean] =
    OptionT(teacherCredsDAO.getByLogin(login))
      .map(_ => true)
      .orElse(OptionT(reviewerCredsDAO.getByLogin(login)).map(_ => true))
      .getOrElse(false)

  def changePassword(teacherId: String, oldPassword: String, newPassword: String): EitherT[F, AppError, String] =
    for {
      creds <- EitherT.fromOptionF(
        teacherCredsDAO.getCredentialsById(teacherId),
        AppError.TeacherByIdDoesNotExists(new IllegalStateException("Teacher doesn't exists")).cast
      )
      isOldPasswordMatchesItsHash <- EitherT.liftF(hashGenerator.checkPassword(oldPassword, creds.passwordHash))
      _                           <- EitherT.cond(isOldPasswordMatchesItsHash, (), AppError.IncorrectOldPassword().cast)
      newHash                     <- EitherT.liftF(hashGenerator.hashPassword(newPassword))
      newBearer                   <- EitherT.liftF(tokenGenerator.generate)
      _                           <- EitherT.liftF(teacherCredsDAO.deleteCredentials(creds.id, creds.login))
      _                           <- EitherT.liftF(teacherCredsDAO.insertCredentials(creds.copy(passwordHash = newHash, bearer = newBearer)))
    } yield newBearer

  def updateTeacher(
    firstName: String,
    lastName: String,
    middleName: Option[String],
    teacherId: String
  ): EitherT[F, AppError, Unit] =
    EitherT {
      teacherDAO
        .updateTeacher(db.Teacher(teacherId, firstName, lastName, middleName, none))
        .map(_.asRight[AppError])
        .handleError(throwable => AppError.Unexpected(throwable).asLeft[Unit])
    }

  def getTeacher(
    teacherId: String
  ): EitherT[F, AppError, TeacherDetails] = {

    def getTeacher: OptionT[F, Teacher] =
      OptionT(teacherDAO.getTeacherById(teacherId))

    def getGroupsForTeacher: F[List[TeacherGroup]] =
      teacherGroupDAO.getByTeacherId(teacherId)

    def logic: OptionT[F, TeacherDetails] =
      getTeacher.semiflatMap { teacher =>
        getGroupsForTeacher.flatMap { groupLinks =>
          groupLinks
            .map { groupLink =>
              groupDAO.get(groupLink.groupId)
            }
            .flatTraverse(_.map(_.toList))
            .map { groups =>
              TeacherDetails(
                id = teacher.id,
                firstName = teacher.firstName,
                lastName = teacher.lastName,
                middleName = teacher.middleName,
                groups = groups.map(group => GroupName(group.id, group.title))
              )
            }
        }
      }

    EitherT(
      logic
        .toRight(AppError.TeacherDoesNotExists(teacherId).cast)
        .value
        .handleError(throwable => AppError.Unexpected(throwable).asLeft[TeacherDetails])
    )
  }

  def getAllTeachers: EitherT[F, AppError, List[FullTeacher]] =
    EitherT(
      teacherDAO.getAllTeachers
        .map(
          _.map(teacher => FullTeacher(id = teacher.id, firstName = teacher.firstName, lastName = teacher.lastName, middleName = teacher.middleName))
        )
        .map(_.asRight[AppError])
        .handleError(throwable => AppError.Unexpected(throwable).asLeft[List[FullTeacher]])
    )

}

object TeacherService {
  def apply[F[_]](
    tokenGenerator: TokenGenerator[F],
    teacherDAO: TeacherDAO[F],
    teacherCredsDAO: TeacherCredentialsDAO[F],
    reviewerCredsDAO: ReviewerCredentialsDAO[F],
    teacherGroupDAO: TeacherGroupDAO[F],
    groupDAO: GroupDAO[F],
    hashGenerator: HashGenerator[F]
  )(implicit
    M: MonadCancel[F, Throwable]
  ): Resource[F, TeacherService[F]] =
    Resource.pure(new TeacherService(tokenGenerator, teacherDAO, teacherCredsDAO, reviewerCredsDAO, teacherGroupDAO, groupDAO, hashGenerator))
}
