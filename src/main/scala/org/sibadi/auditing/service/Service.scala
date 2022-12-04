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
          .create(db.Teacher(id, firstName, lastName, middleName, none))
          .map(_.asRight[AppError])
          .handleError { case throwable =>
            AppError.Unexpected(throwable).asLeft[Unit]
          }
      )
      // TODO: Create random password
      // TODO: Create password hash
      // TODO: Create bearer
      // TODO: Insert creds
    } yield CreatedTeacher(id = id, password = "") // TODO

}

object Service {
  def apply[F[_]](teacherDAO: TeacherDAO[F])(implicit M: MonadCancel[F, Throwable]): Resource[F, Service[F]] =
    Resource.pure(new Service(teacherDAO))
}
