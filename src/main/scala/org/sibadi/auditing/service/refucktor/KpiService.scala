package org.sibadi.auditing.service.refucktor

import cats.data.EitherT
import cats.effect.MonadCancel
import cats.effect.std.UUIDGen
import cats.syntax.all._
import doobie.syntax.all._
import doobie.util.transactor.Transactor
import org.sibadi.auditing.api.endpoints.model._
import org.sibadi.auditing.db.model.KpiDbModel
import org.sibadi.auditing.syntax.sql._
import org.typelevel.log4cats.Logger
import doobie.postgres.implicits.JavaTimeLocalDateTimeMeta

class KpiService[F[_]: UUIDGen: Logger](
  transactor: Transactor[F]
)(implicit M: MonadCancel[F, Throwable]) {

  def create(topicId: String, body: CreateKPIRequestDto): EitherT[F, ApiError, IdResponseDto] =
    for {
      id <- EitherT.liftF(UUIDGen.randomString)
      _ <-
        sql"""
           INSERT INTO topic_kpi (topicId, kpiId)
           VALUES ($topicId, $id)
           """.update.run.void.eitherT(transactor)
      _ <-
        sql"""
           INSERT INTO kpi (id, title, deleteDt)
           VALUES ($id, ${body.title}, null)
           """.update.run.void.eitherT(transactor)
    } yield IdResponseDto(id = id)

  def delete(topicId: String, kpiId: String): EitherT[F, ApiError, Unit] =
    for {
      _ <- sql"""DELETE FROM kpi WHERE id = $kpiId""".update.run.void.eitherT(transactor)
      _ <- sql"""DELETE FROM topic_kpi WHERE topicId = $topicId AND kpiId = $kpiId""".update.run.void.eitherT(transactor)
    } yield ()

  def edit(kpiId: String, body: EditKpiRequestDto): EitherT[F, ApiError, Unit] =
    sql"""
       UPDATE kpi
          SET title = ${body.name}
        WHERE id = $kpiId
       """.update.run.void.eitherT(transactor)

  def getAll(topicId: String): EitherT[F, ApiError, List[TopicKpiItemResponseDto]] =
    sql"""
       SELECT id, title, deleteDt FROM kpi WHERE id IN (SELECT kpiId FROM topic_kpi WHERE topicId = $topicId)
       """
      .query[KpiDbModel]
      .to[List]
      .eitherT(transactor)
      .map(_.map(k => TopicKpiItemResponseDto(id = k.id, title = k.title)))

}
