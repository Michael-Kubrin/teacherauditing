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
      .in("api" / "admin" / "topics")
      .in(jsonBody[CreateTopicsRequestDto])
      .description("")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.unsafeApply(201)))
  val getApiAdminTopics: Endpoint[String, Unit, ApiError, List[TopicItemResponseDto], Any] =
    endpoint.get
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "topics")
      .description("")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(jsonBody[List[TopicItemResponseDto]])
  val deleteApiAdminTopics: Endpoint[String, String, ApiError, Unit, Any] =
    endpoint.delete
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "topics" / path[String]("topicId"))
      .description("")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.NoContent))

  val putApiAdminTopicsTopicId: Endpoint[String, (String, EditTopicRequestDto), ApiError, Unit, Any] =
    endpoint.put
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "topics" / path[String]("topicId"))
      .in(jsonBody[EditTopicRequestDto])
      .description("")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.NoContent))

}
