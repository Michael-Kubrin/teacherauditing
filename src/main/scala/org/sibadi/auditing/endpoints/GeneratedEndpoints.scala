package org.sibadi.auditing.endpoints

import sttp.tapir.{endpoint, oneOf, oneOfVariant, path, query, statusCode, _}
import io.circe.generic.auto.{exportDecoder, exportEncoder}
import org.sibadi.auditing.domain.ApiError
import org.sibadi.auditing.domain.ApiError.{BadRequest, NotFound}
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe.jsonBody

object GeneratedEndpoints {

  final case class KPI(title: String, scope: Int)

  final case class TopicDto(title: String, kpis: List[KPI])

  final case class TopicsRequest(topics: List[TopicDto])

  final case class TopicsResponse(id: String, createTopic: TopicDto)

  final case class EditTopicRequest(name: String)

  final case class TopicsKpiRequest(title: String)

  final case class TopicKpiResponse(id: String, title: String)

  final case class TopicsKpiResponse(response: List[TopicKpiResponse])

  final case class EditTeacherStatusRequest(newStatus: String)

  final case class CreateTeacherRequest(name: String, surName: String, middleName: Option[String])

  final case class EditTeacherRequest(id: String, name: String, surName: String, middleName: Option[String])

  final case class TeacherResponse(id: String, name: String, surName: String, middleName: Option[String])

  final case class CreateReviewerRequest(name: String, surName: String, middleName: String)

  final case class TeachersResponse(response: List[TeacherResponse])

  final case class ReviewerResponse(id: String, name: String, surName: String, middleName: Option[String])

  final case class ReviewersResponse(response: List[ReviewerResponse])

  final case class EditReviewersRequest(id: String, name: String, surName: String, middleName: Option[String])

  final case class FileId(id: String)

  final case class Reviewer(name: String, surName: String, middleName: Option[String], reviewDt: java.time.LocalDateTime)

  final case class GetPublicKpiResponse(
    topicKpiResponse: TopicKpiResponse,
    status: String,
    scope: Option[Long],
    files: Option[List[FileId]],
    reviewer: Option[Reviewer]
  )

  final case class EstimateRequest(score: Long)

  final case class EditGroupsRequest(nameOfGroup: String)

  final case class GroupsResponse(userName: String, groupId: String)

  final case class EditOneGroupRequest(groupId: String)

  final case class CreateUserRequest(userName: String)

  final case class UserResponse(userName: String, userId: String)

  final case class EditUserRequest(userName: String, role: String)

  final case class ResponseId(id: String)

  val postApiAdminTopics =
    endpoint.post
      .in("api" / "admin" / "topics")
      .in(jsonBody[TopicsRequest])
      .description("")
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

  val getApiAdminTopics =
    endpoint.get
      .in("api" / "admin" / "topics")
      .description("")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(jsonBody[TopicsResponse])
      .description("")

  val deleteApiAdminTopics =
    endpoint.delete
      .in("api" / "admin" / "topics")
      .in(query[String]("topicId"))
      .description("")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(stringBody)
      .description("")

  val putApiAdminTopicsTopicId =
    endpoint.put
      .in("api" / "admin" / "topics" / path[String]("topicId"))
      .in(jsonBody[EditTopicRequest])
      .description("")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(stringBody)
      .description("")

  val postApiAdminTopicsTopicIdKpi =
    endpoint.post
      .in("api" / "admin" / "topics" / path[String]("topicId") / "kpi")
      .in(jsonBody[TopicsKpiRequest])
      .description("")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(jsonBody[ResponseId])
      .description("")

  val getApiAdminTopicsTopicIdKpi =
    endpoint.get
      .in("api" / "admin" / "topics" / path[String]("topicId") / "kpi")
      .description("")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(jsonBody[TopicsKpiResponse])
      .description("")

  val putApiAdminTopicsTopicIdKpiKpiId =
    endpoint.put
      .in("api" / "admin" / "topics" / path[String]("topicId") / "kpi" / path[String]("kpiId"))
      .in(jsonBody[EditTopicRequest])
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(stringBody)
      .description("")

  val deleteApiAdminTopicsTopicIdKpiKpiId =
    endpoint.delete
      .in("api" / "admin" / "topics" / path[String]("topicId") / "kpi" / path[String]("kpiId"))
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(stringBody)
      .description("")

  val putApiAdminTopicsTopicIdKpiKpiIdTeachersTeacherIdStatus =
    endpoint.put
      .in("api" / "admin" / "topics" / path[String]("topicId") / "kpi" / path[String]("kpiId") / "teachers" / path[String]("teacherId") / "status")
      .in(jsonBody[EditTeacherStatusRequest])
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(stringBody)
      .description("")

  val postApiAdminTeachers =
    endpoint.post
      .in("api" / "admin" / "teachers")
      .in(jsonBody[CreateTeacherRequest])
      .description("")
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

  val getApiAdminTeachers =
    endpoint.get
      .in("api" / "admin" / "teachers")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(jsonBody[TeachersResponse])
      .description("")

  val putApiAdminTeachers =
    endpoint.put
      .in("api" / "admin" / "teachers")
      .in(jsonBody[EditTeacherRequest])
      .description("")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(stringBody)
      .description("")

  val postApiAdminReviewers =
    endpoint.post
      .in("api" / "admin" / "reviewers")
      .in(jsonBody[CreateReviewerRequest])
      .description("")
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

  val getApiAdminReviewers =
    endpoint.get
      .in("api" / "admin" / "reviewers")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(jsonBody[ReviewersResponse])
      .description("")

  val putApiAdminReviewersId =
    endpoint.put
      .in("api" / "admin" / "reviewers" / path[String]("id"))
      .in(jsonBody[EditReviewersRequest])
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(jsonBody[ReviewersResponse])
      .description("")

  val postApiAdminTopicsTopicIdKpiKpiIdTeacherTeacherId =
    endpoint.post
      .in("api" / "admin" / "topics" / path[String]("topicId") / "kpi" / path[String]("kpiId") / "teacher" / path[String]("teacherId"))
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(stringBody)
      .description("")

  val getApiPublicTopics =
    endpoint.get
      .in("api" / "public" / "topics")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(jsonBody[TopicsResponse])
      .description("")

  val getApiPublicTopicsTopicIdKpi =
    endpoint.get
      .in("api" / "public" / "topics" / path[String]("topicId") / "kpi")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(jsonBody[GetPublicKpiResponse])
      .description("")

  val postApiPublicTopicsTopicIdKpiKpiIdEstimate =
    endpoint.post
      .in("api" / "public" / "topics" / path[String]("topicId") / "kpi" / path[String]("kpiId") / "estimate")
      .in(jsonBody[EstimateRequest])
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

  // TODO: How to correct write multipartBody for files?
//  val postApiPublicTopicsTopicIdKpiKpiIdFiles =
//    endpoint.get
//      .in("api" / "public" / "topics" / path[String]("topicId") / "kpi" / path[String]("kpiId") / "files")
//      .in(multipartBody(extras.tapir.MultipartFilesArrayCodec.DefaultFile))
//      .errorOut(
//        oneOf[ApiError](
//          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
//          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
//          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
//          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
//        )
//      )
//      .out(jsonBody[ResponseId])
//      .description("")

  // TODO: How to correct write octet-stream for fileId?
//  val postApiPublicTopicsTopicIdKpiKpiIdFilesFileId =
//    endpoint.post
//      .in("api" / "public" / "topics" / path[String]("topicId") / "kpi" / path[String]("kpiId") / "files" / path[String]("fileId"))
//      .errorOut(
//        oneOf[ApiError](
//          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
//          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
//          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
//          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
//        )
//      )
//      .out(jsonBody[application / octet - stream])
//      .description("")

  val postApiAdminGroups =
    endpoint.post
      .in("api" / "admin" / "groups")
      .in(jsonBody[EditGroupsRequest])
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(stringBody)
      .description("")

  val getApiAdminGroups =
    endpoint.get
      .in("api" / "admin" / "groups")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(jsonBody[GroupsResponse])
      .description("")

  val putApiAdminGroupsGroupIdTopicsTopicIdKpiKpiId =
    endpoint.put
      .in("api" / "admin" / "groups" / path[String]("groupId") / "topics" / path[String]("topicId") / "kpi" / path[String]("kpiId"))
      .in(jsonBody[EditOneGroupRequest])
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(stringBody)
      .description("")

  val deleteApiAdminGroupsGroupIdTopicsTopicIdKpiKpiId =
    endpoint.delete
      .in("api" / "admin" / "groups" / path[String]("groupId") / "topics" / path[String]("topicId") / "kpi" / path[String]("kpiId"))
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(stringBody)
      .description("")

  val postApiAdminUsers =
    endpoint.post
      .in("api" / "admin" / "users")
      .in(jsonBody[CreateUserRequest])
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

  val getApiAdminUsers =
    endpoint.get
      .in("api" / "admin" / "users")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(jsonBody[UserResponse])
      .description("")

  val putApiAdminUsersUserIdGroupGroupId =
    endpoint.put
      .in("api" / "admin" / "users" / path[String]("userId") / "group" / path[String]("groupId"))
      .in(jsonBody[EditUserRequest])
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(stringBody)
      .description("")

  val deleteApiAdminUsersUserIdGroupGroupId =
    endpoint.delete
      .in("api" / "admin" / "users" / path[String]("userId") / "group" / path[String]("groupId"))
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(stringBody)
      .description("")
}
