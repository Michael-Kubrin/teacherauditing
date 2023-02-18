package org.sibadi.auditing.service

import cats.effect.{MonadCancel, Resource}

class Service[F[_]]()(implicit M: MonadCancel[F, Throwable]) {}

object Service {
  def apply[F[_]]()(implicit M: MonadCancel[F, Throwable]): Resource[F, Service[F]] =
    Resource.pure(new Service())
}
