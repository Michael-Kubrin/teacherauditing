package org.sibadi.auditing.db

import cats.effect.MonadCancel
import cats.syntax.functor._
import doobie._
import doobie.syntax.all._

class TeacherGroupDAO[F[_]](transactor: Transactor[F])(implicit M: MonadCancel[F, Throwable]) {

  def insert(link: TeacherGroup): F[Unit] =
    sql"""
       INSERT INTO teacher_group (teacherId, groupId)
       VALUES (${link.teacherId}, ${link.groupId})
       """
      .update
      .run
      .void
      .transact(transactor)

  def getByGroupId(groupId: String): F[Option[TeacherGroup]] =
    sql"""
       SELECT teacherId, groupId
       FROM teacher_group
       WHERE groupId = $groupId
       """
      .query[TeacherGroup]
      .option
      .transact(transactor)

  def getByTeacherId(teacherId: String): F[Option[TeacherGroup]] =
    sql"""
       SELECT teacherId, groupId
       FROM teacher_group
       WHERE teacherId = $teacherId
       """
      .query[TeacherGroup]
      .option
      .transact(transactor)

  def delete(link: TeacherGroup): F[Unit] =
    sql"""
       DELETE FROM teacher_group
       WHERE group_id = ${link.groupId} AND teacher_id = ${link.teacherId}
       """
      .update
      .run
      .void
      .transact(transactor)

}
