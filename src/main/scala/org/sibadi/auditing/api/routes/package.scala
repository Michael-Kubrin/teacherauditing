package org.sibadi.auditing.api

import cats.Functor
import cats.syntax.either._
import org.sibadi.auditing.api.model.ApiError
import org.typelevel.log4cats.Logger

package object routes {

  def throwableToUnexpected[F[_]: Logger: Functor, A](throwable: Throwable): F[Either[ApiError, A]] =
    Functor[F].map(Logger[F].error(throwable)("Unexpected error"))(_ =>
      ApiError.InternalError(s"Unexpected error: ${throwable.getMessage}").asLeft[A]
    )

}
