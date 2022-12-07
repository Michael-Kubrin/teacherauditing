package org.sibadi.auditing.api.endpoints

import io.circe.generic.auto.{exportDecoder, exportEncoder}
import io.circe.{Decoder, Encoder}
import org.sibadi.auditing.api.model.ApiError
import ApiError._
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe.jsonBody

object GeneratedEndpoints {

  val postLogin =
    endpoint.post
      .in("api" / "login")
      .in(jsonBody[CreateAccountRequestDto])
      .description("")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(401)).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(500)).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(jsonBody[LoginResponse])

  val editPassword =
    endpoint.put
      .securityIn(auth.bearer[String]())
      .in("api" / "password")
      .description("")
      .in(jsonBody[ChangePasswordRequestDto])
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(401)).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(500)).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(jsonBody[PasswordResponse])

  val postApiAdminTopics =
    endpoint.post
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "topics")
      .in(jsonBody[CreateTopicsRequestDto])
      .description("")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(401)).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(500)).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.unsafeApply(201)))
  val getApiAdminTopics =
    endpoint.get
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "topics")
      .description("")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(jsonBody[List[TopicItemResponseDto]])
  val deleteApiAdminTopics =
    endpoint.delete
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "topics" / path[String]("topicId"))
      .description("")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.unsafeApply(204)).and(stringBody))

  val putApiAdminTopicsTopicId =
    endpoint.put
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "topics" / path[String]("topicId"))
      .in(jsonBody[EditTopicRequestDto])
      .description("")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.unsafeApply(204)).and(stringBody))

  val postApiAdminTopicsTopicIdKpi =
    endpoint.post
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "topics" / path[String]("topicId") / "kpi")
      .in(jsonBody[CreateKPIRequestDto])
      .description("")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(jsonBody[ResponseId])

  val getApiAdminTopicsTopicIdKpi =
    endpoint.get
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "topics" / path[String]("topicId") / "kpi")
      .description("")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(jsonBody[List[TopicKpiResponse]])

  val putApiAdminTopicsTopicIdKpiKpiId =
    endpoint.put
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "topics" / path[String]("topicId") / "kpi" / path[String]("kpiId"))
      .in(jsonBody[EditKpiRequestDto])
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.unsafeApply(204)).and(stringBody))
      .description("")

  val deleteApiAdminTopicsTopicIdKpiKpiId =
    endpoint.delete
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "topics" / path[String]("topicId") / "kpi" / path[String]("kpiId"))
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.unsafeApply(204)).and(stringBody))
      .description("")

  val putApiAdminTopicsTopicIdKpiKpiIdTeachersTeacherIdStatus =
    endpoint.put
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "topics" / path[String]("topicId") / "kpi" / path[String]("kpiId") / "teachers" / path[String]("teacherId") / "status")
      .in(jsonBody[EditTeacherStatusRequest])
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.unsafeApply(204)).and(stringBody))
      .description("")

  val postApiAdminTeachers =
    endpoint.post
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "teachers")
      .in(jsonBody[CreateTeacherRequest])
      .description("")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(jsonBody[ResponseIdPassword])

  val getApiAdminTeachers =
    endpoint.get
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "teachers")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(jsonBody[List[TeacherResponse]])
      .description("")
  val putApiAdminTeachers =
    endpoint.put
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "teachers" / path[String]("teacherId"))
      .in(jsonBody[EditTeacherRequest])
      .description("")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.unsafeApply(204)).and(stringBody))

  val postApiAdminReviewers =
    endpoint.post
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "reviewers")
      .in(jsonBody[CreateReviewerRequest])
      .description("")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(jsonBody[ResponseIdPassword])

  val getApiAdminReviewers =
    endpoint.get
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "reviewers")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(jsonBody[List[ReviewerResponse]])
      .description("")

  val putApiAdminReviewersId =
    endpoint.put
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "reviewers" / path[String]("reviewerId"))
      .in(jsonBody[EditReviewersRequest])
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.unsafeApply(204)).and(stringBody))
      .description("")

  val postApiAdminTopicsTopicIdKpiKpiIdTeacherTeacherId =
    endpoint.post
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "topics" / path[String]("topicId") / "kpi" / path[String]("kpiId") / "teacher" / path[String]("teacherId"))
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.unsafeApply(201)).and(stringBody))
      .description("")

  val deleteApiAdminTopicsTopicIdKpiKpiIdTeacherTeacherId =
    endpoint.delete
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "topics" / path[String]("topicId") / "kpi" / path[String]("kpiId") / "teacher" / path[String]("teacherId"))
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.unsafeApply(204)).and(stringBody))
      .description("")

  val getApiPublicTopics =
    endpoint.get
      .securityIn(auth.bearer[String]())
      .in("api" / "public" / "topics")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(jsonBody[TopicItemResponseDto])
      .description("")

  val getApiPublicTopicsTopicIdKpi =
    endpoint.get
      .securityIn(auth.bearer[String]())
      .in("api" / "public" / "topics" / path[String]("topicId") / "kpi")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(jsonBody[GetPublicKpiResponse])
      .description("")

  val postApiPublicTopicsTopicIdKpiKpiIdEstimate =
    endpoint.post
      .securityIn(auth.bearer[String]())
      .in("api" / "public" / "topics" / path[String]("topicId") / "kpi" / path[String]("kpiId") / "estimate")
      .in(jsonBody[EstimateRequest])
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(jsonBody[ResponseId])
      .description("")

  val postApiPublicTopicsTopicIdKpiKpiIdFiles =
    endpoint.post
      .in("api" / "public" / "topics" / path[String]("topicId") / "kpi" / path[String]("kpiId") / "files")
      .in(fileBody)
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(jsonBody[ResponseId])
      .description("")

  val postApiPublicTopicsTopicIdKpiKpiIdFilesFileId =
    endpoint.get
      .in("api" / "public" / "topics" / path[String]("topicId") / "kpi" / path[String]("kpiId") / "files" / path[String]("fileId"))
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(fileBody)
      .description("")

  val postApiAdminGroups =
    endpoint.post
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "groups")
      .in(jsonBody[CreateGroupRequestDtp])
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.unsafeApply(201)).and(stringBody))
      .description("")

  val getApiAdminGroups =
    endpoint.get
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "groups")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(jsonBody[GroupsResponse])
      .description("")

  val putApiAdminGroupsGroupIdTopicsTopicIdKpiKpiId =
    endpoint.put
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "teachers" / path[String]("teacherId") / "groups")
      .in(jsonBody[PutTeacherToGroupsRequest])
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.unsafeApply(204)).and(stringBody))
      .description("")

  val deleteApiAdminGroupsGroupIdTopicsTopicIdKpiKpiId =
    endpoint.delete
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "teachers" / path[String]("teacherId") / "groups" / path[String]("groupId"))
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.unsafeApply(204)).and(stringBody))
      .description("")

  val allEndpoints = List(
    postLogin,
    editPassword,
    postApiAdminTopics,
    getApiAdminTopics,
    deleteApiAdminTopics,
    putApiAdminTopicsTopicId,
    postApiAdminTopicsTopicIdKpi,
    getApiAdminTopicsTopicIdKpi,
    putApiAdminTopicsTopicIdKpiKpiId,
    deleteApiAdminTopicsTopicIdKpiKpiId,
    putApiAdminTopicsTopicIdKpiKpiIdTeachersTeacherIdStatus,
    postApiAdminTeachers,
    getApiAdminTeachers,
    putApiAdminTeachers,
    postApiAdminReviewers,
    getApiAdminReviewers,
    putApiAdminReviewersId,
    postApiAdminTopicsTopicIdKpiKpiIdTeacherTeacherId,
    deleteApiAdminTopicsTopicIdKpiKpiIdTeacherTeacherId,
    getApiPublicTopics,
    getApiPublicTopicsTopicIdKpi,
    postApiPublicTopicsTopicIdKpiKpiIdEstimate,
    postApiPublicTopicsTopicIdKpiKpiIdFiles,
    postApiPublicTopicsTopicIdKpiKpiIdFilesFileId,
    postApiAdminGroups,
    getApiAdminGroups,
    putApiAdminGroupsGroupIdTopicsTopicIdKpiKpiId,
    deleteApiAdminGroupsGroupIdTopicsTopicIdKpiKpiId
  )

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
    val Waiting                                                  = Value
    val Accepted                                                 = Value
    val Declined                                                 = Value
    val Reviewed                                                 = Value
    implicit val enumDecoder: Decoder[ReviewStatus.ReviewStatus] = Decoder.decodeEnumeration(ReviewStatus)
    implicit val enumEncoder: Encoder[ReviewStatus.ReviewStatus] = Encoder.encodeEnumeration(ReviewStatus)
  }

}
