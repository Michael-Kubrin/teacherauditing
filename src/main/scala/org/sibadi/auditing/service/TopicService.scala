package org.sibadi.auditing.service

import cats.data.EitherT
import cats.effect.{MonadCancel, Resource}
import cats.syntax.all._
import org.sibadi.auditing.db
import org.sibadi.auditing.db._
import org.sibadi.auditing.domain.FullTopic
import org.sibadi.auditing.domain.FullKpi
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
        .void
        .map(_.asRight[AppError])
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
    EitherT(
      topicDAO
        .get(groupId)
        .map(_.asRight[AppError])
        .handleError { case throwable =>
          AppError.TopicDoesNotExists(throwable).asLeft[Option[Topic]]
        }
    )

  def getAllTopics: EitherT[F, AppError, List[FullTopic]] = {
    val dbLogic = for {
      topics <- topicDAO.getAll
      topicsWithKpis <- topics.map { topic =>
        for {
          links <- topicKpiDAO.getByTopicId(topic.id)
          kpis <- links.map(link => kpiDAO.get(link.kpiId)).flatTraverse(f => f.map(_.toList)).map(_.map(kpi => FullKpi(kpi.id, kpi.title)))
        } yield FullTopic(topic.id, topic.title, kpis)
      }.sequence
    } yield topicsWithKpis

    EitherT(
      dbLogic
        .map(_.asRight[AppError])
        .handleError(throwable => AppError.TopicDoesNotExists(throwable).asLeft[List[FullTopic]])
    )
  }

  def updateTopic(topicId: String, topicName: String): EitherT[F, AppError, Unit] =
    EitherT {
      topicDAO
        .update(db.Topic(topicId, topicName, None))
        .map(_.asRight[AppError])
        .handleError(throwable => AppError.Unexpected(throwable).cast.asLeft[Unit])
    }

  def getTopicKpiByKpiId(kpiId: String): EitherT[F, AppError, Option[TopicKpi]] =
    EitherT(
      topicKpiDAO
        .getByKpiId(kpiId)
        .map(_.asRight[AppError])
        .handleError(throwable => AppError.TopicKPIDoesNotExists(throwable).asLeft[Option[TopicKpi]])
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
