package org.sibadi.auditing.api.endpoints

import org.sibadi.auditing.api.model._
import ApiError._
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe.jsonBody
import io.circe.generic.auto._

object TeacherActionsAPI {

  val teacherActionsApi = List(
    getApiPublicTopics,
    getApiPublicTopicsTopicIdKpi,
    postApiPublicTopicsTopicIdKpiKpiIdEstimate,
    postApiPublicTopicsTopicIdKpiKpiIdFiles
  )

  val getApiPublicTopics: Endpoint[String, Unit, ApiError, TopicItemResponseDto, Any] =
    endpoint.get
      .securityIn(auth.bearer[String]())
      .in("api" / "public" / "topicsApi")
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

  val getApiPublicTopicsTopicIdKpi: Endpoint[String, String, ApiError, GetPublicKpiResponse, Any] =
    endpoint.get
      .securityIn(auth.bearer[String]())
      .in("api" / "public" / "topicsApi" / path[String]("topicId") / "kpi")
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

  val postApiPublicTopicsTopicIdKpiKpiIdEstimate: Endpoint[String, (String, String, EstimateRequest), ApiError, ResponseId, Any] =
    endpoint.post
      .securityIn(auth.bearer[String]())
      .in("api" / "public" / "topicsApi" / path[String]("topicId") / "kpi" / path[String]("kpiId") / "estimate")
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

  val postApiPublicTopicsTopicIdKpiKpiIdFiles: Endpoint[Unit, (String, String, _root_.sttp.tapir.TapirFile), ApiError, ResponseId, Any] =
    endpoint.post
      .in("api" / "public" / "topicsApi" / path[String]("topicId") / "kpi" / path[String]("kpiId") / "files")
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

}
