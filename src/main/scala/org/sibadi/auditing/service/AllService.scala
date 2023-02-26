package org.sibadi.auditing.service

import cats.data.EitherT
import cats.effect.std.UUIDGen
import cats.effect.{MonadCancel, Resource}
import doobie.syntax.all._
import doobie.util.transactor.Transactor
import org.sibadi.auditing.api.endpoints._
import org.sibadi.auditing.api.endpoints.model._
import org.sibadi.auditing.syntax.sql._
import org.typelevel.log4cats.Logger

class AllService[F[_]: UUIDGen: Logger](
  transactor: Transactor[F]
)(implicit M: MonadCancel[F, Throwable]) {

  def createGroupEndpointHandle(params: CreateGroupRequestDto): EitherT[F, ApiError, Unit] =
    for {
      name <- EitherT.cond(params.name.isBlank, params.name, emptyNameError)
      uuid <- EitherT.liftF(UUIDGen.randomString)
      _    <- sql"""INSERT INTO "group" (id, title) VALUES ($uuid, $name)""".update.run.eitherT(transactor)
    } yield ()
  def createKpiEndpointHandle(params: (String, CreateKPIRequestDto)): EitherT[F, ApiError, IdResponseDto]            = ???
  def createReviewersEndpointHandle(params: CreateReviewerRequestDto): EitherT[F, ApiError, CredentialsResponseDto]  = ???
  def createTeachersEndpointHandle(params: CreateTeacherRequestDto): EitherT[F, ApiError, CredentialsResponseDto]    = ???
  def createTopicEndpointHandle(params: CreateTopicRequestDto): EitherT[F, ApiError, Unit]                           = ???
  def deleteApiAdminGroupsGroupIdKpiKpiIdHandle(params: (String, String)): EitherT[F, ApiError, Unit]                = ???
  def deleteApiAdminGroupsGroupIdTeacherTeacherIdHandle(params: (String, String)): EitherT[F, ApiError, Unit]        = ???
  def deleteGroupEndpointHandle(params: String): EitherT[F, ApiError, Unit]                                          = ???
  def deleteKpiEndpointHandle(params: (String, String)): EitherT[F, ApiError, Unit]                                  = ???
  def deleteReviewersEndpointHandle(params: String): EitherT[F, ApiError, Unit]                                      = ???
  def deleteTeachersEndpointHandle(params: String): EitherT[F, ApiError, Unit]                                       = ???
  def deleteTopicEndpointHandle(params: String): EitherT[F, ApiError, Unit]                                          = ???
  def editGroupEndpointHandle(params: (String, EditGroupRequestDto)): EitherT[F, ApiError, Unit]                     = ???
  def editKpiEndpointHandle(params: (String, String, EditKpiRequestDto)): EitherT[F, ApiError, Unit]                 = ???
  def editReviewersEndpointHandle(params: (String, EditReviewerRequestDto)): EitherT[F, ApiError, Unit]              = ???
  def editTeachersEndpointHandle(params: (String, EditTeacherRequestDto)): EitherT[F, ApiError, Unit]                = ???
  def editTopicNameEndpointHandle(params: (String, EditTopicRequestDto)): EitherT[F, ApiError, Unit]                 = ???
  def estimateTeacherEndpointHandle(params: (String, String, EstimateTeacherRequestDto)): EitherT[F, ApiError, Unit] = ???
  def fillKpiEndpointHandle(params: (String, String, FillKpiRequestDto)): EitherT[F, ApiError, Unit]                 = ???
  def getAllGroupsEndpointHandle(params: Unit): EitherT[F, ApiError, List[GroupItemResponseDto]]                     = ???
  def getAllKpiEndpointHandle(params: String): EitherT[F, ApiError, List[TopicKpiItemResponseDto]]                   = ???
  def getAllReviewersEndpointHandle(params: Unit): EitherT[F, ApiError, List[ReviewerItemResponseDto]]               = ???
  def getAllTopicsEndpointHandle(params: Unit): EitherT[F, ApiError, List[TopicItemResponseDto]]                     = ???
  def getKpiDataForTeacherHandle(params: (String, String)): EitherT[F, ApiError, KpiDataResponseDto]                 = ???
  def getKpisByTeacherForReviewerHandle(params: (String, String)): EitherT[F, ApiError, KpiReviewerDataResponseDto]  = ???
  def getTeachersEndpointHandle(params: Unit): EitherT[F, ApiError, List[TeacherItemResponseDto]]                    = ???
  def loginEndpointHandle(params: LoginRequestDto): EitherT[F, ApiError, BearerResponseDto]                          = ???
  def putApiAdminGroupsGroupIdKpiKpiIdHandle(params: (String, String)): EitherT[F, ApiError, Unit]                   = ???
  def putApiAdminGroupsGroupIdTeacherTeacherIdHandle(params: (String, String)): EitherT[F, ApiError, Unit]           = ???

}

object AllService {
  def apply[F[_]: UUIDGen: Logger](transactor: Transactor[F])(implicit M: MonadCancel[F, Throwable]): Resource[F, AllService[F]] =
    Resource.pure(new AllService(transactor))
}
