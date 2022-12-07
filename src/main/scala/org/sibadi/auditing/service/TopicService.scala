package org.sibadi.auditing.service

import cats.data.EitherT
import cats.effect.{MonadCancel, Resource}
import cats.syntax.all._
import org.sibadi.auditing.db
import org.sibadi.auditing.db.{KpiDAO, Topic, TopicDAO, TopicKpi, TopicKpiDAO}
import org.sibadi.auditing.domain.errors.AppError

import java.util.UUID

class TopicService[F[_]](
  topicDAO: TopicDAO[F],
  kpiDAO: KpiDAO[F],
  topicKpiDAO: TopicKpiDAO[F]
)(implicit M: MonadCancel[F, Throwable]) {

  def createTopics(topicToKpisMap: Map[String, Set[String]]): EitherT[F, AppError, Unit] =
    EitherT {
      topicToKpisMap
        .map { case (topic, kpis) => createTopic(topic, kpis) }
        .toList
        .sequence
        .map(_.asRight[AppError])
        .void
        .handleError(throwable => AppError.Unexpected(throwable).cast.asLeft[Unit])
    }

  private def createTopic(topicName: String, kpiNames: Set[String]): F[Unit] = {
    val topicId = UUID.randomUUID().toString
    for {
      _           <- topicDAO.insert(db.Topic(topicId, topicName, None))
      createdKpis <- kpiNames.map(createKpi).toList.sequence
      _           <- createdKpis.map(kpiId => createTopicKpiLink(topicId, kpiId)).sequence
    } yield ()
  }

  private def createKpi(name: String): F[String] = {
    val id = UUID.randomUUID().toString
    kpiDAO.insert(db.Kpi(id, name, None)).map(_ => id)
  }

  private def createTopicKpiLink(topicId: String, kpiId: String): F[Unit] =
    topicKpiDAO.insert(db.TopicKpi(topicId, kpiId))

  def getTopic(groupId: String): EitherT[F, AppError, Option[Topic]] =
    for {
      _ <- EitherT(
        topicDAO
          .get(groupId)
          .map(_.asRight[AppError])
          .handleError { case throwable =>
            AppError.TopicDoesNotExists(throwable).asLeft[Option[Topic]]
          }
      )
    } yield ()

  def getAllTopics: EitherT[F, AppError, List[Topic]] =
    EitherT(
      topicDAO.getAll
        .map(_.asRight[AppError])
        .handleError(throwable => AppError.TopicDoesNotExists(throwable).asLeft[List[Topic]])
    )

  def updateTopic(topicId: String, topicToKpisMap: Map[String, Set[String]]): EitherT[F, AppError, Unit] =
    EitherT {
      topicToKpisMap
        .map { case (topic, kpis) => updateTopic(topicId, topic, kpis) }
        .toList
        .sequence
        .map(_.asRight[AppError])
        .void
        .handleError(throwable => AppError.Unexpected(throwable).cast.asLeft[Unit])
    }

  //TODO: How to fix this shit, help please
  private def updateTopic(topicId: String, topicName: String, kpiNames: Set[String]): F[Unit] =
    for {
      _           <- topicDAO.update(db.Topic(topicId, topicName, None))
      updatedKpis <- kpiNames.map(updateKpi).toList.sequence
      _           <- updatedKpis.map(kpiId => updateTopicKpiLink(topicId, kpiId)).sequence
    } yield ()

  private def updateKpi(kpiId: String, name: String): F[String] =
    kpiDAO.insert(db.Kpi(kpiId, name, None)).map(_ => kpiId)

  private def updateTopicKpiLink(topicId: String, kpiId: String): F[Unit] =
    topicKpiDAO.insert(db.TopicKpi(topicId, kpiId))

  //TODO: TopicKpiService:
  def createTopicKpi(): EitherT[F, AppError, Unit] =
    for {
      kpiId   <- EitherT.pure(UUID.randomUUID().toString)
      topicId <- EitherT.pure(UUID.randomUUID().toString)
      _ <- EitherT(
        topicKpiDAO
          .insert(db.TopicKpi(topicId, kpiId))
          .map(_.asRight[AppError])
          .handleError(throwable => AppError.Unexpected(throwable).asLeft[Unit])
      )
    } yield ()

  def getTopicKpiByKpiId(kpiId: String): EitherT[F, AppError, Option[TopicKpi]] =
    EitherT(
      topicKpiDAO
        .getByKpiId(kpiId)
        .map(_.asRight[AppError])
        .handleError(throwable => AppError.TopicKPIDoesNotExists(throwable).asLeft[Option[TopicKpi]])
    )

  def getTopicKpiByTopicId(topicId: String): EitherT[F, AppError, Option[TopicKpi]] =
    EitherT(
      topicKpiDAO
        .getByTopicId(topicId)
        .map(_.asRight[AppError])
        .handleError(throwable => AppError.TopicKPIDoesNotExists(throwable).asLeft[TopicKpi])
    )

  def editTopicKpi(topicId: String, kpiId: String): EitherT[F, AppError, Unit] =
    EitherT(
      topicKpiDAO
        .insert(db.TopicKpi(topicId, kpiId))
        .map(_.asRight[AppError])
        .handleError(throwable => AppError.Unexpected(throwable).asLeft[Unit])
    )

  def deleteTopicKpi(topicId: String, kpiId: String): EitherT[F, AppError, Unit] =
    EitherT(
      topicKpiDAO
        .delete(db.TopicKpi(topicId, kpiId))
        .map(_.asRight[AppError])
        .handleError(throwable => AppError.Unexpected(throwable).asLeft[Unit])
    )

}

object TopicService {
  def apply[F[_]](topicDAO: TopicDAO[F], kpiDAO: KpiDAO[F], topicKpiDAO: TopicKpiDAO[F])(implicit
    M: MonadCancel[F, Throwable]
  ): Resource[F, TopicService[F]] =
    Resource.pure(new TopicService(topicDAO, kpiDAO, topicKpiDAO))
}
