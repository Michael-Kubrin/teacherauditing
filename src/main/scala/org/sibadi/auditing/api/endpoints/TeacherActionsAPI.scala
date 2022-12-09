package org.sibadi.auditing.api.endpoints

import org.sibadi.auditing.api.model._
import ApiError._
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe.jsonBody
import io.circe.generic.auto._

object TeacherActionsAPI {

  def teacherActionsApi = List(
    getApiPublicTopics,
    getApiPublicTopicsTopicIdKpi,
    postApiPublicTopicsTopicIdKpiKpiIdEstimate,
    postApiPublicTopicsTopicIdKpiKpiIdFiles
  )

  def getApiPublicTopics: Endpoint[String, Unit, ApiError, List[TopicItemResponseDto], Any] =
    endpoint.get
      .securityIn(auth.bearer[String]())
      .in("api" / "public" / "topics")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(jsonBody[List[TopicItemResponseDto]])
      .description("")

  def getApiPublicTopicsTopicIdKpi: Endpoint[String, String, ApiError, GetPublicKpiResponse, Any] =
    endpoint.get
      .securityIn(auth.bearer[String]())
      .in("api" / "public" / "topics" / path[String]("topicId") / "kpi")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(jsonBody[GetPublicKpiResponse])
      .description("")

  def postApiPublicTopicsTopicIdKpiKpiIdEstimate: Endpoint[String, (String, String, EstimateRequest), ApiError, Unit, Any] =
    endpoint.post
      .securityIn(auth.bearer[String]())
      .in("api" / "public" / "topics" / path[String]("topicId") / "kpi" / path[String]("kpiId") / "estimate")
      .in(jsonBody[EstimateRequest])
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.Created))
      .description("")

  def postApiPublicTopicsTopicIdKpiKpiIdFiles: Endpoint[String, (String, String, _root_.sttp.tapir.TapirFile), ApiError, Unit, Any] =
    endpoint.post
      .securityIn(auth.bearer[String]())
      .in("api" / "public" / "topics" / path[String]("topicId") / "kpi" / path[String]("kpiId") / "files")
      .in(fileBody)
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(sttp.model.StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.Unauthorized).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(sttp.model.StatusCode.InternalServerError).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .description("")

}
