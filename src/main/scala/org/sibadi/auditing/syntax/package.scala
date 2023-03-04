package org.sibadi.auditing

import cats.data.EitherT
import cats.effect.MonadCancelThrow
import cats.syntax.applicativeError._
import cats.syntax.functor._
import doobie.ConnectionIO
import doobie.util.transactor.Transactor
import org.sibadi.auditing.api.endpoints.model.ApiError
import org.sibadi.auditing.api.ApiErrors.sqlError
import org.typelevel.log4cats.Logger
import doobie.syntax.all._

package object syntax {

  object sql extends SqlOps

}

trait SqlOps {

  implicit class AbobaOps[F[_]: MonadCancelThrow: Logger, A](io: ConnectionIO[A]) {

    def eitherT(transactor: Transactor[F]): EitherT[F, ApiError, A] =
      io.attemptT.transact(transactor).leftSemiflatMap { throwable =>
        Logger[F].error(throwable)(s"SQL error").map(_ => sqlError)
      }

  }

}
