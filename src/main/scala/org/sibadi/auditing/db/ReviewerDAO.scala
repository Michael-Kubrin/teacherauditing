package org.sibadi.auditing.db

import cats.effect.MonadCancel
import cats.syntax.functor._
import doobie._
import doobie.syntax.all._
import doobie.implicits.javasql._
import doobie.postgres.implicits._

class ReviewerDAO[F[_]](transactor: Transactor[F])(implicit M: MonadCancel[F, Throwable]) {

  def createReviewer(reviewer: Reviewer): F[Unit] =
    sql"""
       INSERT INTO reviewer(id, firstName, lastName, middleName, deleteDt)
       VALUES (${reviewer.id}, ${reviewer.firstName}, ${reviewer.lastName}, ${reviewer.middleName}, null);
       """.update.run
      .void
      .transact(transactor)

  def updateReviewer(reviewer: Reviewer): F[Unit] =
    sql"""
       UPDATE reviewer
       SET
        firstName = ${reviewer.firstName},
        lastName = ${reviewer.lastName},
        middleName = ${reviewer.middleName},
        deleteDt = ${reviewer.deleteDt}
       WHERE id = ${reviewer.id};
       """.update.run
      .void
      .transact(transactor)

  def getAllReviewers: F[List[Reviewer]] =
    sql"""
       SELECT id, firstName, lastName, middleName, deleteDt FROM reviewer;
       """
      .query[Reviewer]
      .to[List]
      .transact(transactor)

  def getReviewerById(id: String): F[Option[Reviewer]] =
    sql"""
       SELECT id, firstName, lastName, middleName, deleteDt FROM reviewer
       WHERE id = $id
       """
      .query[Reviewer]
      .option
      .transact(transactor)


  //  def createTopics(topics: Topics): F[Unit] =
  //    sql"""
  //       INSERT INTO topics(topicId, nameOfTopic)
  //       VALUES (${topics.topicId}, ${topics.nameOfTopic});
  //       """.update.run
  //      .map(_ => ())
  //      .transact(transactor)


}
