package org.sibadi.auditing.db

import cats.effect.MonadCancel
import cats.syntax.functor._
import doobie._
import doobie.syntax.all._
import org.sibadi.auditing.db.model._

class ReviewerCredentialsDAO[F[_]](transactor: Transactor[F])(implicit M: MonadCancel[F, Throwable]) {

  def insertCredentials(credentials: ReviewerCredentialsDbModel): F[Unit] =
    sql"""
       INSERT INTO reviewer_credentials (id, login, passwordHash, bearer)
       SELECT ${credentials.id}, ${credentials.login}, ${credentials.passwordHash}, ${credentials.bearer}
       WHERE NOT EXISTS (SELECT 1 FROM reviewer_credentials WHERE login = ${credentials.login})
       """.update.run.void
      .transact(transactor)

  def deleteCredentials(id: String, login: String): F[Unit] =
    sql"""
       DELETE FROM reviewer_credentials WHERE id = $id AND login = $login
       """.update.run.void
      .transact(transactor)

  def getCredentialsByBearer(bearer: String): F[Option[ReviewerCredentialsDbModel]] =
    sql"""
       SELECT id, login, passwordHash, bearer
       FROM reviewer_credentials
       WHERE bearer = $bearer
       """
      .query[ReviewerCredentialsDbModel]
      .option
      .transact(transactor)

  def getByLogin(login: String): F[Option[ReviewerCredentialsDbModel]] =
    sql"""
       SELECT id, login, passwordHash, bearer
       FROM reviewer_credentials
       WHERE login = $login
       """
      .query[ReviewerCredentialsDbModel]
      .option
      .transact(transactor)

}
