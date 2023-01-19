package org.sibadi.auditing.api

import cats.{Applicative, Functor, Monad}
import io.circe.{Decoder, Encoder}
import org.sibadi.auditing.domain.errors.AppError
import org.typelevel.log4cats.Logger
// import io.circe.generic.auto.{exportDecoder, exportEncoder}

package object model {

  final case class FileId(id: String)

  final case class Reviewer(name: String, surName: String, middleName: Option[String], reviewDt: java.time.LocalDateTime)

  final case class PutTeacherToGroupsRequest(groupIds: List[String])

  final case class ResponseId(id: String)

  final case class ResponseIdPassword(id: String, password: String)

  final case class CreateKPIRequestDto(title: String)

  final case class CreateTopicRequestDto(title: String, kpis: List[CreateKPIRequestDto])

  final case class CreateTopicsRequestDto(topics: List[CreateTopicRequestDto])

  final case class TopicItemResponseDto(id: String, title: String, kpis: List[KPIItemResponseDto])

  final case class KPIItemResponseDto(id: String, title: String)

  final case class EditTopicRequestDto(name: String)

  final case class TopicKpiResponse(id: String, title: String)

  final case class EditKpiRequestDto(aboba: String)

  final case class EditTeacherStatusRequest(newstatus: ReviewStatus.ReviewStatus)

  final case class CreateTeacherRequest(name: String, surName: String, middleName: Option[String], login: String)

  final case class TeacherResponse(
    id: String,
    name: String,
    surName: String,
    middleName: Option[String],
    groupNames: List[String]
  )

  final case class EditTeacherRequest(name: String, surName: String, middleName: Option[String])

  final case class CreateReviewerRequest(name: String, surName: String, middleName: Option[String], login: String)

  final case class ReviewerResponse(id: String, name: String, surName: String, middleName: Option[String])

  final case class EditReviewersRequest(name: String, surName: String, middleName: Option[String])

  final case class GetPublicKpiResponse(
    status: ReviewStatus.ReviewStatus,
    score: Option[Long],
    files: Option[List[FileId]],
    reviewer: Option[Reviewer]
  )

  final case class EstimateRequest(score: Long)

  final case class CreateGroupRequestDtp(name: String)

  final case class GroupResponseItemDto(id: String, name: String)

  final case class CreateAccountRequestDto(login: String, password: String)

  final case class ChangePasswordRequestDto(oldPassword: String, NewPassword: String)

  final case class LoginResponse(bearerToken: String)

  final case class PasswordResponse(bearerToken: String)

  object ReviewStatus extends Enumeration {
    type ReviewStatus = Value
    val Waiting: model.ReviewStatus.Value                        = Value("waiting")
    val Accepted: model.ReviewStatus.Value                       = Value("accepted")
    val Declined: model.ReviewStatus.Value                       = Value("declined")
    val Reviewed: model.ReviewStatus.Value                       = Value("reviewed")
    implicit val enumDecoder: Decoder[ReviewStatus.ReviewStatus] = Decoder.decodeEnumeration(ReviewStatus)
    implicit val enumEncoder: Encoder[ReviewStatus.ReviewStatus] = Encoder.encodeEnumeration(ReviewStatus)
  }

  def toApiError[F[_]: Logger : Monad](appError: AppError): F[ApiError] =
    appError match {
      case AppError.Unexpected(t) => 
        Functor[F].map(Logger[F].error(t)("Unexpected error"))(_ => ApiError.InternalError(s"Unexpected error: ${t.getMessage}"))

      case AppError.TeacherDoesNotExists(teacherId) =>
        Functor[F].map(Logger[F].error(s"Teacher not found by id $teacherId"))(_ => ApiError.NotFound("Teacher not found"))

      case AppError.GroupDoesNotExists(groupId) => 
        Functor[F].map(Logger[F].error(s"Group not found by id $groupId"))(_ => ApiError.NotFound("Group not found"))

      case AppError.KpiDoesNotExists(id) => 
        Functor[F].map(Logger[F].error(s"KPI not found by id $id"))(_ => ApiError.NotFound("KPI not found"))

      case AppError.ReviewerDoesNotExists(id) => 
        Functor[F].map(Logger[F].error(s"Reviewer not found by id $id"))(_ => ApiError.NotFound("Reviewer not found"))

      case AppError.TopicDoesNotExists(t) => 
        Functor[F].map(Logger[F].error(t)("Topic doesn't exists"))(_ => ApiError.NotFound("Topic doesn't exists"))

      case AppError.TopicKPIDoesNotExists(t) =>
        Functor[F].map(Logger[F].error(t)("Topic doesn't linked to KPI"))(_ => ApiError.NotFound("TopicKPIDoesNotExists"))

      case AppError.GroupByIdDoesNotExists(t) =>
        Functor[F].map(Logger[F].error(t)(""))(_ => ApiError.NotFound("GroupByIdDoesNotExists"))

      case AppError.EstimateDoesNotExists(topicId, kpiId, teacherId) => 
        Functor[F].map(Logger[F].error(s"EstimateDoesNotExists topicId $topicId kpiId $kpiId teacherId $teacherId"))(_ => ApiError.NotFound("EstimateDoesNotExists"))

      case AppError.KpiByIdDoesNotExists(t) => 
        Functor[F].map(Logger[F].error(t)("KpiByIdDoesNotExists"))(_ => ApiError.NotFound("KpiByIdDoesNotExists"))

      case AppError.TeacherByIdDoesNotExists(t) => 
        Functor[F].map(Logger[F].error(t)("TeacherByIdDoesNotExists"))(_ => ApiError.NotFound("Teacher not found"))

      case AppError.TeacherWithoutGroup(teacherId) => 
        Functor[F].map(Logger[F].error(s"Group not found for teacher $teacherId"))(_ => ApiError.NotFound("Teacher without group"))

      case AppError.LoginExists(login) =>
        Functor[F].map(Logger[F].warn(s"Trying to register user but user with login `$login` already exists"))(_ => ApiError.BadRequest(s"User with login `$login` already exists"))

      case AppError.IncorrectOldPassword() =>
        Functor[F].map(Logger[F].warn(s"User trying to change password but hash doesn't matches"))(_ => ApiError.BadRequest(s"Incorrect old password"))

      case AppError.AdminCantChangePassword() =>
        Applicative[F].pure(ApiError.BadRequest(s"You can't change password").cast)

    }

}
