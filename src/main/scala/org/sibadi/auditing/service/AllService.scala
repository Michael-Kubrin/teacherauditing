package org.sibadi.auditing.service

import cats.data.EitherT
import cats.effect.std.UUIDGen
import cats.effect.{MonadCancel, Resource}
import doobie.util.transactor.Transactor
import org.sibadi.auditing.api.endpoints.model._
import org.typelevel.log4cats.Logger

class AllService[F[_]: UUIDGen: Logger](
  transactor: Transactor[F]
)(implicit M: MonadCancel[F, Throwable]) {

  def estimateTeacherEndpointHandle(params: (String, String, EstimateTeacherRequestDto)): EitherT[F, ApiError, Unit] = ???
  def fillKpiEndpointHandle(params: (String, String, FillKpiRequestDto)): EitherT[F, ApiError, Unit]                 = ???
  def getKpiDataForTeacherHandle(params: (String, String)): EitherT[F, ApiError, KpiDataResponseDto]                 = ???
  def getKpisByTeacherForReviewerHandle(params: (String, String)): EitherT[F, ApiError, KpiReviewerDataResponseDto]  = ???

}

object AllService {
  def apply[F[_]: UUIDGen: Logger](transactor: Transactor[F])(implicit M: MonadCancel[F, Throwable]): Resource[F, AllService[F]] =
    Resource.pure(new AllService(transactor))
}
