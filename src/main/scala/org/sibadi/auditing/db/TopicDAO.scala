package org.sibadi.auditing.db

import cats.effect.MonadCancel
import cats.syntax.functor._
import doobie._
import doobie.syntax.all._
import doobie.postgres.implicits._

class TopicDAO[F[_]](transactor: Transactor[F])(implicit M: MonadCancel[F, Throwable]) {

  def insert(group: Topic): F[Unit] =
    sql"""
       INSERT INTO topic (id, title, deleteDt)
       VALUES (${group.id}, ${group.title}, null)
       """.update.run.void
      .transact(transactor)

  def get(id: String): F[Option[Topic]] =
    sql"""
       SELECT id, title, deleteDt FROM topic
       WHERE id = $id
       """
      .query[Topic]
      .option
      .transact(transactor)

  def getAll: F[List[Topic]] =
    sql"""
       SELECT id, title, deleteDt FROM topic
       """
      .query[Topic]
      .to[List]
      .transact(transactor)

  def update(group: Topic): F[Unit] =
    sql"""
       UPDATE topic
       SET
        title = ${group.title},
        deleteDt = ${group.deleteDt}
       WHERE id = ${group.id}
       """.update.run.void
      .transact(transactor)

}
