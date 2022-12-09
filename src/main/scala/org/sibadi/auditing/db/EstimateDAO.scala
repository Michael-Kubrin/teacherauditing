package org.sibadi.auditing.db

import cats.effect.MonadCancel
import cats.syntax.functor._
import doobie._
import doobie.syntax.all._
import doobie.postgres.implicits._

class EstimateDAO[F[_]](transactor: Transactor[F])(implicit M: MonadCancel[F, Throwable]) {

  def insert(estimate: Estimate): F[Unit] =
    sql"""
       INSERT INTO estimate (topicId, kpiId, groupId, teacherId, status, score, lastReviewerId, lastChangesDt)
       VALUES (${estimate.topicId}, ${estimate.kpiId}, ${estimate.groupId}, ${estimate.teacherId}, ${estimate.status}, ${estimate.score}, ${estimate.lastReviewerId}, ${estimate.lastChangesDt})
       """.update.run.void
      .transact(transactor)

  def get(topicId: String, kpiId: String, teacherId: String): F[Option[Estimate]] =
    sql"""
       SELECT topicId, kpiId, groupId, teacherId, status, score, lastReviewerId, lastChangesDt
       FROM estimate
       WHERE topicId = $topicId
         AND kpiId = $kpiId
         AND teacherId = $teacherId
       """
      .query[Estimate]
      .option
      .transact(transactor)

  def update(estimate: Estimate): F[Unit] =
    sql"""
       UPDATE estimate
       SET
        status = ${estimate.status},
        score = ${estimate.score},
        lastReviewer = ${estimate.lastReviewerId},
        lastChangesDt = ${estimate.lastChangesDt}
       WHERE topicId = ${estimate.topicId}
         AND kpiId = ${estimate.kpiId}
         AND teacherId = ${estimate.teacherId}
       """.update.run.void
      .transact(transactor)

}
