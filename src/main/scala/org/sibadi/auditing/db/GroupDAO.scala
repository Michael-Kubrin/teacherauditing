package org.sibadi.auditing.db

import cats.effect.MonadCancel
import cats.syntax.functor._
import doobie._
import doobie.syntax.all._

import doobie.implicits.javasql._
import doobie.postgres.implicits._

class GroupDAO[F[_]](transactor: Transactor[F])(implicit M: MonadCancel[F, Throwable]) {

  def insert(group: Group): F[Unit] =
    sql"""
       INSERT INTO "group" (id, title, deleteDt)
       VALUES (${group.id}, ${group.title}, null)
       """.update.run.void
      .transact(transactor)

  def get(id: String): F[Option[Group]] =
    sql"""
       SELECT id, title, deleteDt FROM "group"
       WHERE id = $id
       """
      .query[Group]
      .option
      .transact(transactor)

  def getAll: F[List[Group]] =
    sql"""
       SELECT id, title, deleteDt FROM "group"
       """
      .query[Group]
      .to[List]
      .transact(transactor)

  def update(group: Group): F[Unit] =
    sql"""
       UPDATE "group"
       SET
        title = ${group.title},
        deleteDt = ${group.deleteDt}
       WHERE id = ${group.id}
       """.update.run.void
      .transact(transactor)

}
