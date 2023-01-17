package org.sibadi.auditing.api.endpoints

import org.sibadi.auditing.api.model._
import ApiError._
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe.jsonBody
import io.circe.generic.auto._

object TopicsAPI {

  def topicsApi = List(
    postApiAdminTopics,
    getApiAdminTopics,
    deleteApiAdminTopics,
    putApiAdminTopicsTopicId
  )

  def postApiAdminTopics: Endpoint[String, CreateTopicsRequestDto, ApiError, Unit, Any] =
    endpoint.post
      .tag("Topics API")
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
  def getApiAdminTopics: Endpoint[String, Unit, ApiError, List[TopicItemResponseDto], Any] =
    endpoint.get
      .tag("Topics API")
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "topics")
      .description("")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(jsonBody[List[TopicItemResponseDto]])
  def deleteApiAdminTopics: Endpoint[String, String, ApiError, Unit, Any] =
    endpoint.delete
      .tag("Topics API")
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "topics" / path[String]("topicId"))
      .description("")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.NoContent))

  def putApiAdminTopicsTopicId: Endpoint[String, (String, EditTopicRequestDto), ApiError, Unit, Any] =
    endpoint.put
      .tag("Topics API")
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "topics" / path[String]("topicId"))
      .in(jsonBody[EditTopicRequestDto])
      .description("")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.NoContent))

}
