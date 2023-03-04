package org.sibadi.auditing.service.refucktor
import cats.data.EitherT
import cats.effect.MonadCancel
import cats.effect.std.UUIDGen
import cats.syntax.all._
import doobie.syntax.all._
import doobie.util.transactor.Transactor
import org.sibadi.auditing.api.endpoints.model._
import org.sibadi.auditing.db.model.{KpiDbModel, TopicDbModel}
import org.sibadi.auditing.syntax.sql._
import org.typelevel.log4cats.Logger
import doobie.postgres.implicits.JavaTimeLocalDateTimeMeta

class TopicService[F[_]: UUIDGen: Logger](
  transactor: Transactor[F]
)(implicit M: MonadCancel[F, Throwable]) {

  def create(params: CreateTopicRequestDto): EitherT[F, ApiError, Unit] =
    for {
      id <- EitherT.liftF(UUIDGen.randomString)
      _ <-
        sql"""
         INSERT INTO topic (id, title, deleteDt)
         VALUES ($id, ${params.name}, null)
         """.update.run.void.eitherT(transactor)
    } yield ()

  def delete(topicId: String): EitherT[F, ApiError, Unit] =
    for {
      _ <- sql"""DELETE FROM topic WHERE topicId = $topicId""".update.run.void.eitherT(transactor)
      _ <- sql"""DELETE FROM topic_kpi WHERE topicId = $topicId""".update.run.void.eitherT(transactor)
    } yield ()

  def edit(topicId: String, body: EditTopicRequestDto): EitherT[F, ApiError, Unit] =
    sql"""
       UPDATE topic
          SET title = ${body.name}
        WHERE id = $topicId
       """.update.run.void.eitherT(transactor)

  def getAll: EitherT[F, ApiError, List[TopicItemResponseDto]] = {

    def getKpisForTopic(topicId: String): EitherT[F, ApiError, List[KpiDbModel]] =
      sql"""
         SELECT id, title, deleteDt FROM kpi WHERE id IN (SELECT kpiId FROM topic_kpi WHERE topicId = $topicId)
         """
        .query[KpiDbModel]
        .to[List]
        .eitherT(transactor)

    for {
      topics <- sql"""SELECT id, title, deleteDt FROM topic""".query[TopicDbModel].to[List].eitherT(transactor)
      fullTopics <- topics
        .map(topic =>
          getKpisForTopic(topic.id).map { kpis =>
            TopicItemResponseDto(id = topic.id, title = topic.title, kpis = kpis.map(kpi => KPIItemResponseDto(kpi.id, kpi.title)))
          }
        )
        .sequence
    } yield fullTopics
  }

}
