package org.sibadi.auditing.service

import cats.effect.{MonadCancel, Resource}
import doobie.util.transactor.Transactor
import org.sibadi.auditing.api.endpoints.model._

class AllService[F[_]](
  transactor: Transactor[F]
)(implicit M: MonadCancel[F, Throwable]) {

  def createGroupEndpointHandle(params: CreateGroupRequestDto): F[Either[ApiError, Unit]] = ???
  def createKpiEndpointHandle(params: (String, CreateKPIRequestDto)): F[Either[ApiError, IdResponseDto]] = ???
  def createReviewersEndpointHandle(params: CreateReviewerRequestDto): F[Either[ApiError, CredentialsResponseDto]] = ???
  def createTeachersEndpointHandle(params: CreateTeacherRequestDto): F[Either[ApiError, CredentialsResponseDto]] = ???
  def createTopicEndpointHandle(params: CreateTopicRequestDto): F[Either[ApiError, Unit]] = ???
  def deleteApiAdminGroupsGroupIdKpiKpiIdHandle(params: (String, String)): F[Either[ApiError, Unit]] = ???
  def deleteApiAdminGroupsGroupIdTeacherTeacherIdHandle(params: (String, String)): F[Either[ApiError, Unit]] = ???
  def deleteGroupEndpointHandle(params: String): F[Either[ApiError, Unit]] = ???
  def deleteKpiEndpointHandle(params: (String, String)): F[Either[ApiError, Unit]] = ???
  def deleteReviewersEndpointHandle(params: String): F[Either[ApiError, Unit]] = ???
  def deleteTeachersEndpointHandle(params: String): F[Either[ApiError, Unit]] = ???
  def deleteTopicEndpointHandle(params: String): F[Either[ApiError, Unit]] = ???
  def editGroupEndpointHandle(params: (String, EditGroupRequestDto)): F[Either[ApiError, Unit]] = ???
  def editKpiEndpointHandle(params: (String, String, EditKpiRequestDto)): F[Either[ApiError, Unit]] = ???
  def editReviewersEndpointHandle(params: (String, EditReviewerRequestDto)): F[Either[ApiError, Unit]] = ???
  def editTeachersEndpointHandle(params: (String, EditTeacherRequestDto)): F[Either[ApiError, Unit]] = ???
  def editTopicNameEndpointHandle(params: (String, EditTopicRequestDto)): F[Either[ApiError, Unit]] = ???
  def estimateTeacherEndpointHandle(params: (String, String, EstimateTeacherRequestDto)): F[Either[ApiError, Unit]] = ???
  def fillKpiEndpointHandle(params: (String, String, FillKpiRequestDto)): F[Either[Unit, Unit]] = ???
  def getAllGroupsEndpointHandle(params: Unit): F[Either[ApiError, List[GroupItemResponseDto]]] = ???
  def getAllKpiEndpointHandle(params: String): F[Either[ApiError, List[TopicKpiItemResponseDto]]] = ???
  def getAllReviewersEndpointHandle(params: Unit): F[Either[ApiError, List[ReviewerItemResponseDto]]] = ???
  def getAllTopicsEndpointHandle(params: Unit): F[Either[ApiError, List[TopicItemResponseDto]]] = ???
  def getKpiDataForTeacherHandle(params: (String, String)): F[Either[ApiError, KpiDataResponseDto]] = ???
  def getKpisByTeacherForReviewerHandle(params: (String, String)): F[Either[ApiError, KpiReviewerDataResponseDto]] = ???
  def getTeachersEndpointHandle(params: Unit): F[Either[ApiError, List[TeacherItemResponseDto]]] = ???
  def loginEndpointHandle(params: LoginRequestDto): F[Either[ApiError, BearerResponseDto]] = ???
  def putApiAdminGroupsGroupIdKpiKpiIdHandle(params: (String, String)): F[Either[ApiError, Unit]] = ???
  def putApiAdminGroupsGroupIdTeacherTeacherIdHandle(params: (String, String)): F[Either[ApiError, Unit]] = ???

}

object AllService {
  def apply[F[_]](transactor: Transactor[F])(implicit M: MonadCancel[F, Throwable]): Resource[F, AllService[F]] =
    Resource.pure(new AllService(transactor))
}
