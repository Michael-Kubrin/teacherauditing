package org.sibadi.auditing.service

import cats.syntax.option._
import cats.data.EitherT
import cats.effect.{MonadCancel, Resource}
import cats.syntax.all._
import com.roundeights.hasher.Algo
import org.sibadi.auditing.db
import org.sibadi.auditing.db.{Teacher, TeacherCredentialsDAO, TeacherDAO}
import org.sibadi.auditing.domain._
import org.sibadi.auditing.domain.errors.AppError

import java.util.UUID
import scala.io.Source

class TeacherService[F[_]](
  teacherDAO: TeacherDAO[F],
  teacherCredsDAO: TeacherCredentialsDAO[F]
)(implicit M: MonadCancel[F, Throwable]) {

  def createTeacher(
    firstName: String,
    lastName: String,
    middleName: Option[String],
    login: String
  ): EitherT[F, AppError, CreatedTeacher] =
    for {
      id <- EitherT.pure(UUID.randomUUID().toString)
      _ <- EitherT(
        teacherDAO
          .createTeacher(db.Teacher(id, firstName, lastName, middleName, none))
          .map(_.asRight[AppError])
          .handleError { case throwable =>
            AppError.Unexpected(throwable).asLeft[Unit]
          }
      )
      // TODO: Create random password
      password <- EitherT.pure(randomPassword(10))
      // TODO: Create password hash
      source = Algo.sha1.tap(Source.fromString(password))
      hash         <- source.hash
      passwordHash <- password.map(x => hash(x)).toString
      // TODO: Create bearer
      bearer <- EitherT.pure(UUID.randomUUID().toString)
      // TODO: Insert creds
      _ <- EitherT.liftF(teacherCredsDAO.insertCredentials(db.TeacherCredentials(id, login, passwordHash, bearer))) // TODO
    } yield CreatedTeacher(id = id, password = password) // TODO

  def updateTeacher(
    firstName: String,
    lastName: String,
    middleName: Option[String],
    login: String,
    teacherId: String
  ): EitherT[F, AppError, Unit] =
    for {
      id <- EitherT.pure(UUID.randomUUID().toString)
      _ <- EitherT(
        teacherDAO
          .updateTeacher(db.Teacher(id, firstName, lastName, middleName, none))
          .map(_.asRight[AppError])
          .handleError { case throwable =>
            AppError.Unexpected(throwable).asLeft[Unit]
          }
      )
      password <- EitherT.pure(randomPassword(10))
      source = Algo.sha1.tap(Source.fromString(password))
      hash         <- source.hash
      passwordHash <- password.map(x => hash(x)).toString
      bearer       <- EitherT.pure(UUID.randomUUID().toString)
      _            <- EitherT.liftF(teacherCredsDAO.insertCredentials(db.TeacherCredentials(id, login, passwordHash, bearer))) // TODO
    } yield ()

  def getTeacher(
    teacherId: String
  ): EitherT[F, AppError, Teacher] =
    for {
      _ <- EitherT(
        teacherDAO
          .getTeacherById(teacherId)
          .map(_.asRight[AppError])
          .handleError { case throwable =>
            AppError.TeacherDoesNotExists(throwable).asLeft[Teacher]
          }
      )
    } yield ()

  def getAllTeachers: EitherT[F, AppError, List[Teacher]] =
    for {
      _ <- EitherT(
        teacherDAO.getAllTeachers
          .map(_.asRight[AppError])
          .handleError { case throwable =>
            AppError.TeacherDoesNotExists(throwable).asLeft[List[Teacher]]
          }
      )
    } yield ()

  private def randomPassword(len: Int): String = {
    val randomize     = new scala.util.Random(System.nanoTime)
    val stringBuilder = new StringBuilder(len)
    val password      = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
    for (i <- 0 until len)
      stringBuilder.append(password(randomize.nextInt(password.length)))
    stringBuilder.toString
  }
}

object TeacherService {
  def apply[F[_]](teacherDAO: TeacherDAO[F], teacherCredsDAO: TeacherCredentialsDAO[F])(implicit
    M: MonadCancel[F, Throwable]
  ): Resource[F, TeacherService[F]] =
    Resource.pure(new TeacherService(teacherDAO, teacherCredsDAO))
}
