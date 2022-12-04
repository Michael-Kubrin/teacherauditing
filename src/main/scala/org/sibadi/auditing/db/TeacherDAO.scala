package org.sibadi.auditing.db

import cats.effect.MonadCancel
import doobie._
import doobie.syntax.all._

class TeacherDAO[F[_]](transactor: Transactor[F])(implicit M: MonadCancel[F, Throwable]) {

  // UPDATE
  // SELECT ALL
  // SELECT ONE
  // DELETE ONE

  // FIXME Option[java.time.LocalDateTime]
  def create(teacher: Teacher): F[Unit] =
    sql"""
       INSERT INTO teacher(id, firstName, lastName, middleName, deleteDt)
       VALUES (${teacher.id}, ${teacher.firstName}, ${teacher.lastName}, ${teacher.middleName}, null);
       """
      .update
      .run
      .map(_ => ())
      .transact(transactor)

}
