package org.sibadi.auditing.api

import cats.syntax.applicativeError._
import cats.syntax.either._
import cats.{ApplicativeError, Functor}
import org.sibadi.auditing.api.endpoints.model.ApiError
import org.sibadi.auditing.service.Authenticator
import org.typelevel.log4cats.Logger

package object routes {

  def adminSecurityLogic[F[_]: Functor: Logger](
    bearerToken: String
  )(implicit authenticator: Authenticator[F], applicativeError: ApplicativeError[F, Throwable]): F[Either[ApiError, Authenticator.UserType]] =
    authenticator
      .isAdmin(bearerToken)
      .toRight(ApiError.Unauthorized("Unauthorized").cast)
      .value
      .handleErrorWith(throwableToUnexpected[F, Authenticator.UserType])

  def throwableToUnexpected[F[_]: Logger: Functor, A](throwable: Throwable): F[Either[ApiError, A]] =
    Functor[F].map(Logger[F].error(throwable)("Unexpected error"))(_ => ApiError.InternalError(s"Unexpected error: ${throwable.getMessage}").asLeft[A])

}
