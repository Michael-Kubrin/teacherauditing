package org.sibadi.auditing.service

import cats.data.EitherT
import cats.effect.{MonadCancel, Resource}
import cats.syntax.all._
import org.sibadi.auditing.db
import org.sibadi.auditing.db.TeacherDAO
import org.sibadi.auditing.domain._
import org.sibadi.auditing.domain.errors.AppError

import java.util.UUID

class Service[F[_]](
  teacherDAO: TeacherDAO[F]
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
      // TODO: Create bearer
      bearer <- EitherT.pure(UUID.randomUUID().toString)
      // TODO: Insert creds
    } yield CreatedTeacher(id = id, password = password) // TODO

  def updateTeacher(
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
    } yield UpdatedTeacher()

  private def randomPassword(len: Int): String = {
    val randomize     = new scala.util.Random(System.nanoTime)
    val stringBuilder = new StringBuilder(len)
    val password      = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
    for (i <- 0 until len)
      stringBuilder.append(password(randomize.nextInt(password.length)))
    stringBuilder.toString
  }

}

object Service {
  def apply[F[_]](teacherDAO: TeacherDAO[F])(implicit M: MonadCancel[F, Throwable]): Resource[F, Service[F]] =
    Resource.pure(new Service(teacherDAO))
}
