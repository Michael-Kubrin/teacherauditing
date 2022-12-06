package org.sibadi.auditing.service

import cats.syntax.option._
import cats.data.EitherT
import cats.effect.{MonadCancel, Resource}
import cats.syntax.all._
import org.sibadi.auditing.api.endpoints.GeneratedEndpoints.CreateTopicRequestDto
import org.sibadi.auditing.db
import org.sibadi.auditing.db.{Topic, TopicDAO}
import org.sibadi.auditing.domain._
import org.sibadi.auditing.domain.errors.AppError

import java.util.UUID

class TopicService[F[_]](
  topicDAO: TopicDAO[F]
)(implicit M: MonadCancel[F, Throwable]) {

  // TODO: It's not right
  def createTopic(kpis: List[CreateTopicRequestDto], groupId: String, title: String): EitherT[F, AppError, CreatedTopic] =
    for {
      id <- EitherT.pure(UUID.randomUUID().toString)
      _ <- EitherT(
        topicDAO
          .insert(db.Topic(id, title, none))
          .map(_.asRight[AppError])
          .handleError { case throwable =>
            AppError.Unexpected(throwable).asLeft[Unit]
          }
      )
    } yield CreatedTopic(groupId = groupId, title = title)

  def getTopic(groupId: String): EitherT[F, AppError, Topic] =
    for {
      _ <- EitherT(
        topicDAO
          .get(groupId)
          .map(_.asRight[AppError])
          .handleError { case throwable =>
            AppError.TopicDoesNotExists(throwable).asLeft[Topic]
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
