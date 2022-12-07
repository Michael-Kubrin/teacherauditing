package org.sibadi.auditing.service

import cats.data.EitherT
import cats.effect.{MonadCancel, Resource}
import cats.syntax.all._
import com.roundeights.hasher.Implicits._
import org.sibadi.auditing.db
import org.sibadi.auditing.db.{Teacher, TeacherCredentialsDAO, TeacherDAO, TeacherGroup, TeacherGroupDAO}
import org.sibadi.auditing.domain._
import org.sibadi.auditing.domain.errors.AppError
import org.sibadi.auditing.util.{PasswordGenerator, TokenGenerator}

import java.util.UUID

class TeacherService[F[_]](
  tokenGenerator: TokenGenerator[F],
  teacherDAO: TeacherDAO[F],
  teacherCredsDAO: TeacherCredentialsDAO[F],
  teacherGroupDAO: TeacherGroupDAO[F]
)(implicit M: MonadCancel[F, Throwable]) {

  def createTeacher(
    firstName: String,
    lastName: String,
    middleName: Option[String],
    login: String
  ): EitherT[F, AppError, CreatedTeacher] =
    for {
      teacherId <- EitherT.pure(UUID.randomUUID().toString)
      _ <- EitherT(
        teacherDAO
          .createTeacher(db.Teacher(teacherId, firstName, lastName, middleName, none))
          .map(_.asRight[AppError])
          .handleError(throwable => AppError.Unexpected(throwable).asLeft[Unit])
      )
      password = PasswordGenerator.randomPassword(10)
      hash     = password.bcrypt.hex
      bearer <- EitherT.liftF(tokenGenerator.generate)
      _      <- EitherT.liftF(teacherCredsDAO.insertCredentials(db.TeacherCredentials(id, login, hash, bearer)))
    } yield CreatedTeacher(id = teacherId, password = password)

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
  ): EitherT[F, AppError, Teacher] =
    EitherT(
      teacherDAO
        .getTeacherById(teacherId)
        .map(_.toRight(AppError.TeacherDoesNotExists(teacherId).cast))
        .handleError(throwable => AppError.Unexpected(throwable).asLeft[Teacher])
    )

  def getAllTeachers: EitherT[F, AppError, List[Teacher]] =
    EitherT(
      teacherDAO.getAllTeachers
        .map(_.asRight[AppError])
        .handleError(throwable => AppError.Unexpected(throwable).asLeft[List[Teacher]])
    )

  //TODO: TeacherGroupService:

  def createGroup: EitherT[F, AppError, Unit] =
    for {
      teacherId <- EitherT.pure(UUID.randomUUID().toString) // TODO: Not sure, that we must create teacherId here
      groupId   <- EitherT.pure(UUID.randomUUID().toString)
      _ <- EitherT(
        teacherGroupDAO
          .insert(db.TeacherGroup(teacherId, groupId))
          .map(_.asRight[AppError])
          .handleError(throwable => AppError.Unexpected(throwable).asLeft[Unit])
      )
    } yield ()

  def getByGroupId(groupId: String): EitherT[F, AppError, Option[TeacherGroup]] =
    EitherT(
      teacherGroupDAO
        .getByGroupId(groupId)
        .map(_.asRight[AppError])
        .handleError(throwable => AppError.GroupByIdDoesNotExists(throwable).asLeft[Option[TeacherGroup]])
    )

  def getByTeacherId(teacherId: String): EitherT[F, AppError, Option[TeacherGroup]] =
    EitherT(
      teacherGroupDAO
        .getByTeacherId(teacherId)
        .map(_.asRight[AppError])
        .handleError(throwable => AppError.TeacherByIdDoesNotExists(throwable).asLeft[Option[TeacherGroup]])
    )

  //TODO: Which type of Link has to be need written?
  def deleteGroup(link: ???): EitherT[F, AppError, Unit] =
    EitherT(
      teacherGroupDAO
        .delete(link)
        .map(_.asRight[AppError])
        .handleError(throwable => AppError.Unexpected(throwable).asLeft[Unit])
    )

}

object TeacherService {
  def apply[F[_]](tokenGenerator: TokenGenerator[F], teacherDAO: TeacherDAO[F], teacherCredsDAO: TeacherCredentialsDAO[F])(implicit
    M: MonadCancel[F, Throwable]
  ): Resource[F, TeacherService[F]] =
    Resource.pure(new TeacherService(tokenGenerator, teacherDAO, teacherCredsDAO))
}
