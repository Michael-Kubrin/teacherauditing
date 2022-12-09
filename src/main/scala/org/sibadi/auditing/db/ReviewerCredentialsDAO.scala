package org.sibadi.auditing.db

import cats.effect.MonadCancel
import cats.syntax.functor._
import doobie._
import doobie.syntax.all._
import doobie.implicits.javasql._
import doobie.postgres.implicits._

class ReviewerCredentialsDAO[F[_]](transactor: Transactor[F])(implicit M: MonadCancel[F, Throwable]) {

  def insertCredentials(credentials: ReviewerCredentials): F[Unit] =
    sql"""
       INSERT INTO reviewer_credentials (id, login, passwordHash, bearer)
       VALUES (${credentials.id}, ${credentials.login}, ${credentials.passwordHash}, ${credentials.bearer}
       """
      .update
      .run
      .void
      .transact(transactor)

  def deleteCredentials(id: String, login: String): F[Unit] =
    sql"""
       DELETE FROM reviewer_credentials WHERE id = $id AND login = $login
       """
      .update
      .run
      .void
      .transact(transactor)

  def getCredentialsByBearer(bearer: String): F[Option[ReviewerCredentials]] =
    sql"""
       SELECT id, login, passwordHash, bearer
       FROM reviewer_credentials
       WHERE bearer = $bearer
       """
      .query[ReviewerCredentials]
      .option
      .transact(transactor)

  def getCredentialsById(id: String): F[Option[ReviewerCredentials]] =
    sql"""
       SELECT id, login, passwordHash, bearer
       FROM reviewer_credentials
       WHERE id = $id
       """
      .query[ReviewerCredentials]
      .option
      .transact(transactor)

}
