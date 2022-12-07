package org.sibadi.auditing.service

import cats.syntax.option._
import cats.data.EitherT
import cats.effect.{MonadCancel, Resource}
import cats.syntax.all._
import org.sibadi.auditing.db
import org.sibadi.auditing.db.{KpiDAO, Topic, TopicDAO}
import org.sibadi.auditing.domain._
import org.sibadi.auditing.domain.errors.AppError

import java.util.UUID

class KpiService[F[_]](kpiDAO: KpiDAO[F])(implicit M: MonadCancel[F, Throwable]) {

  //def createKpi(): EitherT[F, AppError, ]

}

object KpiService {
  def apply[F[_]](kpiDAO: KpiDAO[F])(implicit M: MonadCancel[F, Throwable]): Resource[F, KpiService[F]] =
    Resource.pure(new KpiService(kpiDAO))
}
