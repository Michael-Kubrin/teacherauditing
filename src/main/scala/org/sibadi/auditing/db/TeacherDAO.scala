package org.sibadi.auditing.db

import cats.effect.MonadCancel
import doobie._
import doobie.syntax.all._
//import org.sibadi.auditing.service.Authenticator

class TeacherDAO[F[_]](transactor: Transactor[F])(implicit M: MonadCancel[F, Throwable]) {

  // SELECT ALL
  // SELECT ONE
  // DELETE ONE

  // FIXME Option[java.time.LocalDateTime]
  def createTeacher(teacher: Teacher): F[Unit] =
    sql"""
       INSERT INTO teacher(id, firstName, lastName, middleName, deleteDt)
       VALUES (${teacher.id}, ${teacher.firstName}, ${teacher.lastName}, ${teacher.middleName}, null);
       """.update.run
      .map(_ => ())
      .transact(transactor)

  def updateTeacher(teacher: Teacher): F[Unit] =
    sql"""
       INSERT INTO teacher(id, firstName, lastName, middleName, deleteDt)
       VALUES (${teacher.id}, ${teacher.firstName}, ${teacher.lastName}, ${teacher.middleName}, null);
       """.update.run
      .map(_ => ())
      .transact(transactor)

  // TODO: how to create get?
//  def getAllTeachers: F[Unit] =
//    sql"""
//         SELECT FROM teacher(id, firstName, lastName, middleName, deleteDt);
//           """
//      .query[Authenticator.UserType]
//      .map(_ => ())

  def editTeacher(teacher: Teacher): F[Unit] =
    sql"""
       INSERT INTO teacher(id, firstName, lastName, middleName, deleteDt)
       VALUES (${teacher.id}, ${teacher.firstName}, ${teacher.lastName}, ${teacher.middleName}, null);
       """.update.run
      .map(_ => ())
      .transact(transactor)

//  def createTopics(topics: Topics): F[Unit] =
//    sql"""
//       INSERT INTO topics(topicId, nameOfTopic)
//       VALUES (${topics.topicId}, ${topics.nameOfTopic});
//       """.update.run
//      .map(_ => ())
//      .transact(transactor)

  def createReviewer(reviewer: Reviewer): F[Unit] =
    sql"""
       INSERT INTO teacher(id, firstName, lastName, middleName, deleteDt)
       VALUES (${reviewer.id}, ${reviewer.firstName}, ${reviewer.lastName}, ${reviewer.middleName}, null);
       """.update.run
      .map(_ => ())
      .transact(transactor)

}
