package org.sibadi.auditing.service

import cats.data.EitherT
import cats.effect.{MonadCancel, Resource}
import cats.syntax.all._
import org.sibadi.auditing.db
import org.sibadi.auditing.db.{TopicKpi, TopicKpiDAO}
import org.sibadi.auditing.domain._
import org.sibadi.auditing.domain.errors.AppError

import java.util.UUID

class TopicKpiService[F[_]](topicKpiDAO: TopicKpiDAO[F])(implicit M: MonadCancel[F, Throwable]) {

  def createTopicKpi(kpiId: String, topicId: String): EitherT[F, AppError, CreatedTopicKPI] =
    for {
      id <- EitherT.pure(UUID.randomUUID().toString)
      _ <- EitherT(
        topicKpiDAO
          .insert(db.TopicKpi(topicId, kpiId))
          .map(_.asRight[AppError])
          .handleError { case throwable =>
            AppError.Unexpected(throwable).asLeft[Unit]
          }
      )
    } yield CreatedTopicKPI(kpiId = kpiId, topicId = topicId)

  def getTopicKpiByKpiId(kpiId: String): EitherT[F, AppError, TopicKpi] =
    for {
      _ <- EitherT(
        topicKpiDAO
          .getByKpiId(kpiId)
          .map(_.asRight[AppError])
          .handleError { case throwable =>
            AppError.TopicKPIDoesNotExists(throwable).asLeft[TopicKpi]
          }
      )
    } yield ()

  def getTopicKpiByTopicId(topicId: String): EitherT[F, AppError, TopicKpi] =
    for {
      _ <- EitherT(
        topicKpiDAO
          .getByTopicId(topicId)
          .map(_.asRight[AppError])
          .handleError { case throwable =>
            AppError.TopicKPIDoesNotExists(throwable).asLeft[TopicKpi]
          }
      )
    } yield ()

  def editTopicKpi(topicId: String, kpiId: String): EitherT[F, AppError, TopicKpi] =
    for {
      _ <- EitherT(
        topicKpiDAO
          .insert(db.TopicKpi(topicId, kpiId))
          .map(_.asRight[AppError])
          .handleError { case throwable =>
            AppError.Unexpected(throwable).asLeft[Unit]
          }
      )
    } yield ()

  def deleteTopicKpi(topicId: String, kpiId: String): EitherT[F, AppError, TopicKpi] =
    for {
      _ <- EitherT(
        topicKpiDAO
          .delete(db.TopicKpi(topicId, kpiId))
          .map(_.asRight[AppError])
          .handleError { case throwable =>
            AppError.Unexpected(throwable).asLeft[Unit]
          }
      )
    } yield ()
}

object TopicKpiService {

  def apply[F[_]](topicKpiDAO: TopicKpiDAO[F])(implicit M: MonadCancel[F, Throwable]): Resource[F, TopicKpiService[F]] =
    Resource.pure(new TopicKpiService(topicKpiDAO))
}
