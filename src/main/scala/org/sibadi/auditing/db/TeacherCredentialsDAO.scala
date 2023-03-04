package org.sibadi.auditing.db

import cats.effect.MonadCancel
import cats.syntax.functor._
import doobie._
import doobie.syntax.all._
import org.sibadi.auditing.db.model._

class TeacherCredentialsDAO[F[_]](transactor: Transactor[F])(implicit M: MonadCancel[F, Throwable]) {

  def insertCredentials(credentials: TeacherCredentialsDbModel): F[Unit] =
    sql"""
       INSERT INTO teacher_credentials (id, login, passwordHash, bearer)
       SELECT ${credentials.id}, ${credentials.login}, ${credentials.passwordHash}, ${credentials.bearer}
       WHERE NOT EXISTS (SELECT 1 FROM teacher_credentials WHERE login = ${credentials.login})
       """.update.run.void
      .transact(transactor)

  def deleteCredentials(id: String, login: String): F[Unit] =
    sql"""
       DELETE FROM teacher_credentials WHERE id = $id AND login = $login
       """.update.run.void
      .transact(transactor)

  def getCredentialsByBearer(bearer: String): F[Option[TeacherCredentialsDbModel]] =
    sql"""
       SELECT id, login, passwordHash, bearer
       FROM teacher_credentials
       WHERE bearer = $bearer
       """
      .query[TeacherCredentialsDbModel]
      .option
      .transact(transactor)

  def getByLogin(login: String): F[Option[TeacherCredentialsDbModel]] =
    sql"""
       SELECT id, login, passwordHash, bearer
       FROM teacher_credentials
       WHERE login = $login
       """
      .query[TeacherCredentialsDbModel]
      .option
      .transact(transactor)

}
