package org.sibadi.auditing.service

import cats.data.EitherT
import cats.effect.{MonadCancel, Resource}
import cats.syntax.all._
import org.sibadi.auditing.db
import org.sibadi.auditing.db.{TeacherCredentialsDAO, TeacherDAO}
import org.sibadi.auditing.domain._
import org.sibadi.auditing.domain.errors.AppError

import java.util.UUID

class Service[F[_]](
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
      // TODO: Create bearer
      bearer <- EitherT.pure(UUID.randomUUID().toString)
      // TODO: Insert creds
      _ <- EitherT.liftF(teacherCredsDAO.insertCredentials(db.TeacherCredentials())) // TODO
    } yield CreatedTeacher(id = id, password = password) // TODO

  private def randomPassword(len: Int): String = {
    val randomize     = new scala.util.Random(System.nanoTime)
    val stringBuilder = new StringBuilder(len)
    val password      = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
    for (i <- 0 until len)
      stringBuilder.append(password(randomize.nextInt(password.length)))
    stringBuilder.toString
  }

  def updateTeacher(
    firstName: String,
    lastName: String,
    middleName: Option[String],
    login: String
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
    } yield ()

  //TODO: I don't know how to create table
//  def createTopics(
//    topics: List[CreateTopicRequestDto]
//  ): EitherT[F, AppError, CreateTopics] =
//    for {
//      id <- EitherT.pure(UUID.randomUUID().toString)
//      _ <- EitherT(
//        teacherDAO.createTopics(db.Topics())
//      )
//    }

  def editTeacher(
    name: String,
    surName: String,
    middleName: Option[String]
  ): EitherT[F, AppError, EditedTeacher] =
    for {
      id <- EitherT.pure(UUID.randomUUID().toString)
      _ <- EitherT(
        teacherDAO
          .updateTeacher(db.Teacher(id, name, surName, middleName, none))
          .map(_.asRight[AppError])
          .handleError { case throwable =>
            AppError.Unexpected(throwable).asLeft[Unit]
          }
      )
      password <- EitherT.pure(randomPassword(10))
      bearer   <- EitherT.pure(UUID.randomUUID().toString)
    } yield EditedTeacher(id, password)

  def createReviewer(
    firstName: String,
    lastName: String,
    middleName: Option[String],
    login: String
  ): EitherT[F, AppError, CreatedReviewer] =
    for {
      id <- EitherT.pure(UUID.randomUUID().toString)
      _ <- EitherT(
        teacherDAO
          .createReviewer(db.Reviewer(id, firstName, lastName, middleName, none))
          .map(_.asRight[AppError])
          .handleError { case throwable =>
            AppError.Unexpected(throwable).asLeft[Unit]
          }
      )
      password <- EitherT.pure(randomPassword(10))
      bearer   <- EitherT.pure(UUID.randomUUID().toString)
    } yield CreatedReviewer(id = id, password = password) // TODO

}

object Service {
  def apply[F[_]](teacherDAO: TeacherDAO[F])(implicit M: MonadCancel[F, Throwable]): Resource[F, Service[F]] =
    Resource.pure(new Service(teacherDAO))
}
