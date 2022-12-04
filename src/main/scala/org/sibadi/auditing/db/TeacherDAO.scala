package org.sibadi.auditing.db

import cats.effect.MonadCancel
import doobie._
import doobie.syntax.all._
import org.sibadi.auditing.service.Authenticator

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

  def getAllTeachers: F[Unit] =
    sql"""
         SELECT FROM teacher(id, firstName, lastName, middleName, deleteDt);
           """
      .query[Authenticator.UserType]
      .map(_ => ())
}
