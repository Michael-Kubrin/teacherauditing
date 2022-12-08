package org.sibadi.auditing.service

import cats.data.EitherT
import cats.effect.{MonadCancel, Resource}
import cats.syntax.all._
import org.sibadi.auditing.db
import org.sibadi.auditing.db.{Kpi, KpiDAO, KpiGroup, KpiGroupDAO}
import org.sibadi.auditing.domain.errors.AppError

import java.util.UUID

class KpiService[F[_]](kpiDAO: KpiDAO[F], kpiGroupDAO: KpiGroupDAO[F])(implicit M: MonadCancel[F, Throwable]) {

  def createKpi(title: String): EitherT[F, AppError, Unit] =
    for {
      id <- EitherT.pure(UUID.randomUUID().toString)
      _ <- EitherT(
        kpiDAO
          .insert(db.Kpi(id, title, None))
          .map(_.asRight[AppError])
          .handleError(throwable => AppError.Unexpected(throwable).asLeft[Unit])
      )
    } yield ()

  def updateKpi(id: String, title: String): EitherT[F, AppError, Unit] =
    EitherT(
      kpiDAO
        .update(db.Kpi(id, title, None))
        .map(_.asRight[AppError])
        .handleError(throwable => AppError.Unexpected(throwable).asLeft[Unit])
    )

  def getKpi(kpiId: String): EitherT[F, AppError, Kpi] =
    EitherT(
      kpiDAO
        .get(kpiId)
        .map(_.toRight(AppError.KpiDoesNotExists(kpiId).cast))
        .handleError(throwable => AppError.Unexpected(throwable).asLeft[Kpi])
    )

  def getAllKpi: EitherT[F, AppError, List[Kpi]] =
    EitherT(
      kpiDAO.getAll
        .map(_.asRight[AppError])
        .handleError(throwable => AppError.Unexpected(throwable).asLeft[List[Kpi]])
    )

  def linkGroupToKpi(groupId: String, kpiId: String): EitherT[F, AppError, Unit] =
    for {
      _ <- EitherT(
        kpiGroupDAO
          .insert(db.KpiGroup(kpiId, groupId))
          .map(_.asRight[AppError])
          .handleError(throwable => AppError.Unexpected(throwable).asLeft[Unit])
      )
    } yield ()

  def getByGroupId(groupId: String): EitherT[F, AppError, Option[KpiGroup]] =
    EitherT(
      kpiGroupDAO
        .getByGroupId(groupId)
        .map(_.asRight[AppError])
        .handleError(throwable => AppError.GroupByIdDoesNotExists(throwable).asLeft[Option[KpiGroup]])
    )

  def getByKpiId(kpiId: String): EitherT[F, AppError, Option[KpiGroup]] =
    EitherT(
      kpiGroupDAO
        .getByKpiId(kpiId)
        .map(_.asRight[AppError])
        .handleError(throwable => AppError.KpiByIdDoesNotExists(throwable).asLeft[Option[KpiGroup]])
    )

}

object KpiService {
  def apply[F[_]](kpiDAO: KpiDAO[F], kpiGroupDAO: KpiGroupDAO[F])(implicit M: MonadCancel[F, Throwable]): Resource[F, KpiService[F]] =
    Resource.pure(new KpiService(kpiDAO, kpiGroupDAO))
}
