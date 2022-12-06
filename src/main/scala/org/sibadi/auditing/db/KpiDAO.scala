package org.sibadi.auditing.db

import cats.effect.MonadCancel
import cats.syntax.functor._
import doobie._
import doobie.syntax.all._

class KpiDAO[F[_]](transactor: Transactor[F])(implicit M: MonadCancel[F, Throwable]) {

  def insert(kpi: Kpi): F[Unit] =
    sql"""
       INSERT INTO kpi (id, title, deleteDt)
       VALUES (${kpi.id}, ${kpi.title}, null)
       """.update.run.void
      .transact(transactor)

  def get(id: String): F[Option[Kpi]] =
    sql"""
       SELECT id, title, deleteDt FROM kpi
       WHERE id = $id
       """
      .query[Kpi]
      .option
      .transact(transactor)

  def getAll: F[List[Kpi]] =
    sql"""
       SELECT id, title, deleteDt FROM kpi
       """
      .query[Kpi]
      .to[List]
      .transact(transactor)

  def update(kpi: Kpi): F[Unit] =
    sql"""
       UPDATE kpi
       SET
        title = ${kpi.title},
        deleteDt = ${kpi.deleteDt}
       WHERE id = ${kpi.id}
       """.update.run.void
      .transact(transactor)

}
