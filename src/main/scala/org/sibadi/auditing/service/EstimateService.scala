package org.sibadi.auditing.service

import cats.data.EitherT
import cats.effect.{MonadCancel, Resource}
import cats.syntax.all._
import org.sibadi.auditing.db
import org.sibadi.auditing.db.EstimateDAO
import org.sibadi.auditing.domain._
import org.sibadi.auditing.domain.errors.AppError

import java.util.UUID

class EstimateService[F[_]](estimateDAO: EstimateDAO[F])(implicit M: MonadCancel[F, Throwable]) {

  def createEstimate(topicId: String, kpiId: String, teacherId: String): EitherT[F, AppError, CreatedEstimated] =
    for {
      id <- EitherT.pure(UUID.randomUUID().toString)
      _ <- EitherT(
        estimateDAO
          .insert(db.Estimate(topicId, kpiId, ???, teacherId, ???, ???, ???))
          .map(_.asRight[AppError])
          .handleError { case throwable =>
            AppError.Unexpected(throwable).asLeft[Unit]
          }
      )
    } yield CreatedEstimated()

  def updateEstimate(topicId: String, kpiId: String, teacherId: String): EitherT[F, AppError, CreatedEstimated] =
    for {
      id <- EitherT.pure(UUID.randomUUID().toString)
      _ <- EitherT(
        estimateDAO
          .update(db.Estimate(topicId, kpiId, ???, teacherId, ???, ???, ???))
          .map(_.asRight[AppError])
          .handleError { case throwable =>
            AppError.Unexpected(throwable).asLeft[Unit]
          }
      )
    } yield ()

  def getEstimate(): EitherT[F, AppError, CreatedEstimated] =
    for {
      id <- EitherT.pure(UUID.randomUUID().toString)
      _ <- EitherT(
        estimateDAO
          .get(???, ???, ???)
          .map(_.asRight[AppError])
          .handleError { case throwable =>
            AppError.Unexpected(throwable).asLeft[Unit]
          }
      )
    } yield ()
}

object EstimateService {
  def apply[F[_]](estimateDAO: EstimateDAO[F])(implicit M: MonadCancel[F, Throwable]): Resource[F, EstimateService[F]] =
    Resource.pure(new EstimateService(estimateDAO))
}
