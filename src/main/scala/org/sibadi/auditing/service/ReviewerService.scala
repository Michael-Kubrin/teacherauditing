package org.sibadi.auditing.service

import cats.data.EitherT
import cats.effect.{MonadCancel, Resource}
import cats.syntax.all._
import com.roundeights.hasher.Implicits._
import org.sibadi.auditing.db
import org.sibadi.auditing.db.{Reviewer, ReviewerCredentialsDAO, ReviewerDAO}
import org.sibadi.auditing.domain._
import org.sibadi.auditing.domain.errors.AppError
import org.sibadi.auditing.util.{PasswordGenerator, TokenGenerator}

import java.util.UUID

class ReviewerService[F[_]](
  tokenGenerator: TokenGenerator[F],
  reviewerDAO: ReviewerDAO[F],
  reviewerCredsDAO: ReviewerCredentialsDAO[F]
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
          .handleError(throwable => AppError.Unexpected(throwable).asLeft[Unit])
      )
      password = PasswordGenerator.randomPassword(10)
      hash     = password.bcrypt.hex
      bearer <- EitherT.liftF(tokenGenerator.generate)
      _      <- EitherT.liftF(reviewerCredsDAO.insertCredentials(db.ReviewerCredentials(id, login, hash, bearer)))
    } yield CreatedReviewer(id = id, password = password)

  def updateReviewer(
    firstName: String,
    lastName: String,
    middleName: Option[String],
    reviewerId: String
  ): EitherT[F, AppError, Unit] =
    EitherT {
      reviewerDAO
        .updateReviewer(db.Reviewer(reviewerId, firstName, lastName, middleName, none))
        .map(_.asRight[AppError])
        .handleError(throwable => AppError.Unexpected(throwable).asLeft[Unit])
    }

  def getReviewer(
    reviewerId: String
  ): EitherT[F, AppError, Reviewer] =
    EitherT(
      reviewerDAO
        .getReviewerById(reviewerId)
        .map(_.toRight(AppError.ReviewerDoesNotExists(reviewerId).cast))
        .handleError(throwable => AppError.Unexpected(throwable).asLeft[Reviewer])
    )

  def getAllReviewers: EitherT[F, AppError, List[Reviewer]] =
    EitherT(
      reviewerDAO.getAllReviewers
        .map(_.asRight[AppError])
        .handleError(throwable => AppError.Unexpected(throwable).asLeft[List[Reviewer]])
    )

}

object ReviewerService {
  def apply[F[_]](tokenGenerator: TokenGenerator[F], reviewerDAO: ReviewerDAO[F], reviewerCredsDAO: ReviewerCredentialsDAO[F])(implicit
    M: MonadCancel[F, Throwable]
  ): Resource[F, ReviewerService[F]] =
    Resource.pure(new ReviewerService(tokenGenerator, reviewerDAO, reviewerCredsDAO))
}
