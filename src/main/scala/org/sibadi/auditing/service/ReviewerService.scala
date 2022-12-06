package org.sibadi.auditing.service

import cats.data.EitherT
import cats.effect.{MonadCancel, Resource}
import cats.syntax.all._
import cats.syntax.option.none
import com.roundeights.hasher.Algo
import org.sibadi.auditing.db
import org.sibadi.auditing.db.{Reviewer, ReviewerCredentialsDAO, ReviewerDAO}
import org.sibadi.auditing.domain.CreatedReviewer
import org.sibadi.auditing.domain.errors.AppError

import java.util.UUID
import scala.io.Source

class ReviewerService[F[_]](
  reviewerDAO: ReviewerDAO[F],
  reviewerCredDAO: ReviewerCredentialsDAO[F]
)(implicit M: MonadCancel[F, Throwable]) {

  def createReviewer(
    firstName: String,
    lastName: String,
    middleName: Option[String],
    login: String
  ): EitherT[F, AppError, CreatedReviewer] =
    for {
      id <- EitherT.pure(UUID.randomUUID().toString)
      _ <- EitherT(
        reviewerDAO
          .createReviewer(db.Reviewer(id, firstName, lastName, middleName, none))
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
      _            <- EitherT.liftF(reviewerCredDAO.insertCredentials(db.ReviewerCredentials(id, login, passwordHash, bearer)))
    } yield CreatedReviewer(id = id, password = password)

  private def randomPassword(len: Int): String = {
    val randomize     = new scala.util.Random(System.nanoTime)
    val stringBuilder = new StringBuilder(len)
    val password      = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
    for (i <- 0 until len)
      stringBuilder.append(password(randomize.nextInt(password.length)))
    stringBuilder.toString
  }

  def getReviewer(
    reviewerId: String
  ): EitherT[F, AppError, Reviewer] =
    for {
      _ <- EitherT(
        reviewerDAO
          .getReviewerById(reviewerId)
          .map(_.asRight[AppError])
          .handleError { case throwable =>
            AppError.ReviewerDoesNotExists(throwable).asLeft[Reviewer]
          }
      )
    } yield ()

  def getAllReviewers: EitherT[F, AppError, List[Reviewer]] =
    for {
      _ <- EitherT(
        reviewerDAO.getAllReviewers
          .map(_.asRight[AppError])
          .handleError { case throwable =>
            AppError.ReviewerDoesNotExists(throwable).asLeft[List[Reviewer]]
          }
      )
    } yield ()

  def updateReviewer(
    firstName: String,
    lastName: String,
    middleName: Option[String],
    login: String,
    reviewerId: String
  ): EitherT[F, AppError, Unit] =
    for {
      id <- EitherT.pure(UUID.randomUUID().toString)
      _ <- EitherT(
        reviewerDAO
          .updateReviewer(db.Reviewer(id, firstName, lastName, middleName, none))
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
      _            <- EitherT.liftF(reviewerCredDAO.insertCredentials(db.ReviewerCredentials(id, login, passwordHash, bearer))) // TODO
    } yield ()
}

object ReviewerService {
  def apply[F[_]](reviewerDAO: ReviewerDAO[F], reviewerCredsDAO: ReviewerCredentialsDAO[F])(implicit
    M: MonadCancel[F, Throwable]
  ): Resource[F, ReviewerService[F]] =
    Resource.pure(new ReviewerService(reviewerDAO, reviewerCredsDAO))
}
