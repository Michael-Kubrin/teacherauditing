package org.sibadi.auditing.service.refucktor

import cats.data.EitherT
import cats.effect.MonadCancel
import cats.effect.std.UUIDGen
import doobie.syntax.all._
import doobie.util.transactor.Transactor
import org.sibadi.auditing.api.ApiErrors
import org.sibadi.auditing.api.endpoints.model._
import org.sibadi.auditing.syntax.sql._
import org.sibadi.auditing.util.{HashGenerator, PasswordGenerator, TokenGenerator}
import org.typelevel.log4cats.Logger

class RegisterService[F[_]: UUIDGen: Logger](
  transactor: Transactor[F],
  tokenGenerator: TokenGenerator[F],
  hashGenerator: HashGenerator[F]
)(implicit M: MonadCancel[F, Throwable]) {

  def registerReviewer(
    rawFirstName: String,
    rawLastName: String,
    rawMiddleName: Option[String],
    rawLogin: String
  ): EitherT[F, ApiError, CredentialsResponseDto] =
    for {
      firstName   <- EitherT.cond(!rawFirstName.isBlank, rawFirstName, ApiErrors.emptyNameError)
      lastName    <- EitherT.cond(!rawLastName.isBlank, rawLastName, ApiErrors.emptyNameError)
      middleName  <- EitherT.cond(rawMiddleName.fold(true)(s => !s.isBlank), rawMiddleName, ApiErrors.emptyNameError)
      login       <- EitherT.cond(!rawLogin.isBlank, rawLogin, ApiErrors.emptyNameError)
      anyTeacher  <- sql"""SELECT 1 FROM teacher_credentials WHERE login = $login""".query[Int].option.eitherT(transactor)
      _           <- EitherT.cond(anyTeacher.isEmpty, (), ApiErrors.loginExists)
      anyReviewer <- sql"""SELECT 1 FROM reviewer_credentials WHERE login = $login""".query[Int].option.eitherT(transactor)
      _           <- EitherT.cond(anyReviewer.isEmpty, (), ApiErrors.loginExists)
      id          <- EitherT.liftF(UUIDGen.randomString)
      password = PasswordGenerator.randomPassword(10)
      hash   <- EitherT.liftF(hashGenerator.hashPassword(password))
      bearer <- EitherT.liftF(tokenGenerator.generate)
      _ <-
        sql"""
           INSERT INTO teacher(id, firstName, lastName, middleName, deleteDt)
           VALUES ($id, $firstName, $lastName, $middleName, null)
           """.update.run.eitherT(transactor)
      _ <-
        sql"""
           INSERT INTO teacher_credentials (id, login, passwordHash, bearer)
           SELECT $id, $login, $hash, $bearer
           WHERE NOT EXISTS (SELECT 1 FROM teacher_credentials WHERE login = $login)
           """.update.run.eitherT(transactor)
    } yield CredentialsResponseDto(id, password)

  def registerTeacher(
    rawFirstName: String,
    rawLastName: String,
    rawMiddleName: Option[String],
    rawLogin: String
  ): EitherT[F, ApiError, CredentialsResponseDto] =
    for {
      firstName   <- EitherT.cond(!rawFirstName.isBlank, rawFirstName, ApiErrors.emptyNameError)
      lastName    <- EitherT.cond(!rawLastName.isBlank, rawLastName, ApiErrors.emptyNameError)
      middleName  <- EitherT.cond(rawMiddleName.fold(true)(s => !s.isBlank), rawMiddleName, ApiErrors.emptyNameError)
      login       <- EitherT.cond(!rawLogin.isBlank, rawLogin, ApiErrors.emptyNameError)
      anyTeacher  <- sql"""SELECT 1 FROM teacher_credentials WHERE login = $login""".query[Int].option.eitherT(transactor)
      _           <- EitherT.cond(anyTeacher.isEmpty, (), ApiErrors.loginExists)
      anyReviewer <- sql"""SELECT 1 FROM reviewer_credentials WHERE login = $login""".query[Int].option.eitherT(transactor)
      _           <- EitherT.cond(anyReviewer.isEmpty, (), ApiErrors.loginExists)
      id          <- EitherT.liftF(UUIDGen.randomString)
      password = PasswordGenerator.randomPassword(10)
      hash   <- EitherT.liftF(hashGenerator.hashPassword(password))
      bearer <- EitherT.liftF(tokenGenerator.generate)
      _ <-
        sql"""
         INSERT INTO reviewer(id, firstName, lastName, middleName, deleteDt)
         VALUES ($id, $firstName, $lastName, $middleName, null)
         """.update.run.eitherT(transactor)
      _ <-
        sql"""
         INSERT INTO reviewer_credentials (id, login, passwordHash, bearer)
         SELECT $id, $login, $hash, $bearer
         WHERE NOT EXISTS (SELECT 1 FROM reviewer_credentials WHERE login = $login)
         """.update.run.eitherT(transactor)
    } yield CredentialsResponseDto(id, password)

}
