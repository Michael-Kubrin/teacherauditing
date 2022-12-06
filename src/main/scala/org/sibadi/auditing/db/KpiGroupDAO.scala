package org.sibadi.auditing.db

import cats.effect.MonadCancel
import cats.syntax.functor._
import doobie._
import doobie.syntax.all._

class KpiGroupDAO[F[_]](transactor: Transactor[F])(implicit M: MonadCancel[F, Throwable]) {

  def insert(link: KpiGroup): F[Unit] =
    sql"""
       INSERT INTO kpi_group (kpiId, groupId)
       VALUES (${link.kpiId}, ${link.groupId})
       """
      .update
      .run
      .void
      .transact(transactor)

  def getByGroupId(groupId: String): F[Option[KpiGroup]] =
    sql"""
       SELECT kpiId, groupId
       FROM kpi_group
       WHERE groupId = $groupId
       """
      .query[KpiGroup]
      .option
      .transact(transactor)

  def getByKpiId(kpiId: String): F[Option[KpiGroup]] =
    sql"""
       SELECT kpiId, groupId
       FROM kpi_group
       WHERE kpiId = $kpiId
       """
      .query[KpiGroup]
      .option
      .transact(transactor)

  def delete(link: KpiGroup): F[Unit] =
    sql"""
       DELETE FROM kpi_group
       WHERE group_id = ${link.groupId} AND kpi_id = ${link.kpiId}
       """
      .update
      .run
      .void
      .transact(transactor)

}
