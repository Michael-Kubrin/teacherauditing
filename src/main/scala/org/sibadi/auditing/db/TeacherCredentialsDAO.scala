package org.sibadi.auditing.db

import cats.effect.MonadCancel
import cats.syntax.functor._
import doobie._
import doobie.syntax.all._

class TeacherCredentialsDAO[F[_]](transactor: Transactor[F])(implicit M: MonadCancel[F, Throwable]) {

  def insertCredentials(credentials: TeacherCredentials): F[Unit] =
    sql"""
       INSERT INTO teacher_credentials (id, login, passwordHash, bearer)
       SELECT ${credentials.id}, ${credentials.login}, ${credentials.passwordHash}, ${credentials.bearer}
       FROM teacher_credentials
       WHERE NOT EXISTS (SELECT 1 FROM teacher_credentials WHERE login = ${credentials.login})
       """.update.run.void
      .transact(transactor)

  def deleteCredentials(id: String, login: String): F[Unit] =
    sql"""
       DELETE FROM teacher_credentials WHERE id = $id AND login = $login
       """.update.run.void
      .transact(transactor)

  def getCredentialsByBearer(bearer: String): F[Option[TeacherCredentials]] =
    sql"""
       SELECT id, login, passwordHash, bearer
       FROM teacher_credentials
       WHERE bearer = $bearer
       """
      .query[TeacherCredentials]
      .option
      .transact(transactor)

  def getCredentialsById(id: String): F[Option[TeacherCredentials]] =
    sql"""
       SELECT id, login, passwordHash, bearer
       FROM teacher_credentials
       WHERE id = $id
       """
      .query[TeacherCredentials]
      .option
      .transact(transactor)

  def getByLoginAndPassword(login: String): F[Option[TeacherCredentials]] =
    sql"""
       SELECT id, login, passwordHash, bearer
       FROM teacher_credentials
       WHERE login = $login
       """
      .query[TeacherCredentials]
      .option
      .transact(transactor)

}
