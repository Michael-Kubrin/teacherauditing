package org.sibadi.auditing.db

import cats.effect.MonadCancel
import cats.syntax.functor._
import doobie._
import doobie.syntax.all._
import doobie.postgres.implicits._

class TeacherDAO[F[_]](transactor: Transactor[F])(implicit M: MonadCancel[F, Throwable]) {

  // FIXME Option[java.time.LocalDateTime]
  def createTeacher(teacher: Teacher): F[Unit] =
    sql"""
       INSERT INTO teacher(id, firstName, lastName, middleName, deleteDt)
       VALUES (${teacher.id}, ${teacher.firstName}, ${teacher.lastName}, ${teacher.middleName}, null)
       """.update.run.void
      .transact(transactor)

  def updateTeacher(teacher: Teacher): F[Unit] =
    sql"""
       UPDATE teacher
       SET
        firstName = ${teacher.firstName},
        lastName = ${teacher.lastName},
        middleName = ${teacher.middleName},
        deleteDt = ${teacher.deleteDt}
       WHERE id = ${teacher.id};
       """.update.run.void
      .transact(transactor)

  def getAllTeachers: F[List[Teacher]] =
    sql"""
       SELECT id, firstName, lastName, middleName, deleteDt FROM teacher;
       """
      .query[Teacher]
      .to[List]
      .transact(transactor)

  def getTeacherById(id: String): F[Option[Teacher]] =
    sql"""
       SELECT id, firstName, lastName, middleName, deleteDt FROM teacher
       WHERE id = $id
       """
      .query[Teacher]
      .option
      .transact(transactor)

}
