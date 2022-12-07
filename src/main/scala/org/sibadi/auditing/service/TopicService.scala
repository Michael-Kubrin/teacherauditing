package org.sibadi.auditing.service

import cats.data.EitherT
import cats.effect.{MonadCancel, Resource}
import cats.syntax.all._
import org.sibadi.auditing.db
import org.sibadi.auditing.db.{KpiDAO, Topic, TopicDAO, TopicKpiDAO}
import org.sibadi.auditing.domain._
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
    for {
      _ <- EitherT(
        topicDAO.getAll
          .map(_.asRight[AppError])
          .handleError { case throwable =>
            AppError.TeacherDoesNotExists(throwable).asLeft[List[Topic]]
          }
      )
    } yield ()

  def updateTopic(kpis: List[CreateTopicRequestDto], groupId: String, title: String): EitherT[F, AppError, Unit] =
    for {
      id <- EitherT.pure(UUID.randomUUID().toString)
      _ <- EitherT(
        topicDAO
          .update(db.Topic(id, title, none))
          .map(_.asRight[AppError])
          .handleError { case throwable =>
            AppError.Unexpected(throwable).asLeft[Unit]
          }
      )
    } yield ()

}

object TopicService {
  def apply[F[_]](topicDAO: TopicDAO[F])(implicit
    M: MonadCancel[F, Throwable]
  ): Resource[F, TopicService[F]] =
    Resource.pure(new TopicService(topicDAO))
}
