package org.sibadi.auditing.api

import io.circe.{Decoder, Encoder}
import org.sibadi.auditing.domain.errors.AppError
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

  final case class GroupsResponse(id: String, name: String)

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

  def toApiError(appError: AppError): ApiError =
    appError match {
      case t => ApiError.NotFound(t.toString)
    }

}
