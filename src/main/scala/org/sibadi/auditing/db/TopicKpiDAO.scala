package org.sibadi.auditing.db

import cats.effect.MonadCancel
import cats.syntax.functor._
import doobie._
import doobie.syntax.all._

class TopicKpiDAO[F[_]](transactor: Transactor[F])(implicit M: MonadCancel[F, Throwable]) {

  def insert(link: TopicKpi): F[Unit] =
    sql"""
       INSERT INTO topic_kpi (topicId, kpiId)
       VALUES (${link.topicId}, ${link.kpiId})
       """
      .update
      .run
      .void
      .transact(transactor)

  def getByTopicId(topicId: String): F[Option[TopicKpi]] =
    sql"""
       SELECT topicId, kpiId
       FROM topic_kpi
       WHERE topicId = $topicId
       """
      .query[TopicKpi]
      .option
      .transact(transactor)

  def getByKpiId(kpiId: String): F[Option[TopicKpi]] =
    sql"""
       SELECT topicId, kpiId
       FROM topic_kpi
       WHERE kpiId = $kpiId
       """
      .query[TopicKpi]
      .option
      .transact(transactor)

  def delete(link: TopicKpi): F[Unit] =
    sql"""
       DELETE FROM topic_kpi
       WHERE topic_id = ${link.topicId} AND kpi_id = ${link.kpiId}
       """
      .update
      .run
      .void
      .transact(transactor)

}
