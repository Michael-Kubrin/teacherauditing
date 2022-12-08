package org.sibadi.auditing.api.endpoints

import org.sibadi.auditing.api.model._
import ApiError._
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe.jsonBody
import io.circe.generic.auto._

object TopicsAPI {

  val topicsApi = List(
    postApiAdminTopics,
    getApiAdminTopics,
    deleteApiAdminTopics,
    putApiAdminTopicsTopicId,
  )

  val postApiAdminTopics: Endpoint[String, CreateTopicsRequestDto, ApiError, Unit, Any] =
    endpoint.post
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "topicsApi")
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
  val getApiAdminTopics: Endpoint[String, Unit, ApiError, List[TopicItemResponseDto], Any] =
    endpoint.get
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "topicsApi")
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
  val deleteApiAdminTopics: Endpoint[String, String, ApiError, String, Any] =
    endpoint.delete
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "topicsApi" / path[String]("topicId"))
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

  val putApiAdminTopicsTopicId: Endpoint[String, (String, EditTopicRequestDto), ApiError, String, Any] =
    endpoint.put
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "topicsApi" / path[String]("topicId"))
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

}
