package org.sibadi.auditing.api.endpoints

import io.circe.generic.auto._
import org.sibadi.auditing.api.model.ApiError._
import org.sibadi.auditing.api.model._
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe.jsonBody

object TopicsAPI {

  def topicsApi = List(
    postApiAdminTopics,
    getApiAdminTopics,
    deleteApiAdminTopics,
    putApiAdminTopicsTopicId
  )

  def postApiAdminTopics: Endpoint[String, CreateTopicsRequestDto, ApiError, Unit, Any] =
    baseEndpoint.post
      .tag("Topics API")
      .securityIn(auth.bearer[String]())
      .in("topics")
      .in(jsonBody[CreateTopicsRequestDto])
      .description("Создание раздела")
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
    baseEndpoint.get
      .tag("Topics API")
      .securityIn(auth.bearer[String]())
      .in("topics")
      .description("Получение всех разделов")
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
    baseEndpoint.delete
      .tag("Topics API")
      .securityIn(auth.bearer[String]())
      .in("topics" / path[String]("topicId"))
      .description("Удаление раздела")
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
    baseEndpoint.put
      .tag("Topics API")
      .securityIn(auth.bearer[String]())
      .in("topics" / path[String]("topicId"))
      .in(jsonBody[EditTopicRequestDto])
      .description("Внесение изменений в раздел")
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
