package org.sibadi.auditing.routes

import cats.Applicative
import cats.data.EitherT
import org.sibadi.auditing.validators.ValidationErrors
import org.sibadi.auditing.domain.AppError

trait RouterOps[F[_]] {

  protected def convertValidationErrors[A](errors: ValidationErrors)(implicit A: Applicative[F]): EitherT[F, AppError, A] =
    EitherT.leftT[F, A](errors.head)

}
