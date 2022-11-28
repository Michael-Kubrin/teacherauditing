package org.sibadi.auditing.routes

import cats.Applicative
import cats.data.EitherT
import org.sibadi.auditing.validators.ValidationErrors

trait RouterOps[F[_]] {

  protected def convertValidationErrors[A](errors: ValidationErrors)(implicit A: Applicative[F]): EitherT[F, AuditingError, A] =
    EitherT.leftT[F, A](errors.head)

}
