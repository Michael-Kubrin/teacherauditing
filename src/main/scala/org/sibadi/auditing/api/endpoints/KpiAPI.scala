package org.sibadi.auditing.api.endpoints

import org.sibadi.auditing.api.model._
import ApiError._
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe.jsonBody
import io.circe.generic.auto._

object KpiAPI {

  val kpiApi = List(
    postApiAdminTopicsTopicIdKpi,
    getApiAdminTopicsTopicIdKpi,
    putApiAdminTopicsTopicIdKpiKpiId,
    deleteApiAdminTopicsTopicIdKpiKpiId,
  )

  val postApiAdminTopicsTopicIdKpi: Endpoint[String, (String, CreateKPIRequestDto), ApiError, ResponseId, Any] =
    endpoint.post
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "topics" / path[String]("topicId") / "kpi")
      .in(jsonBody[CreateKPIRequestDto])
      .description("")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(jsonBody[ResponseId])

  val getApiAdminTopicsTopicIdKpi: Endpoint[String, String, ApiError, List[TopicKpiResponse], Any] =
    endpoint.get
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "topics" / path[String]("topicId") / "kpi")
      .description("")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(jsonBody[List[TopicKpiResponse]])

  val putApiAdminTopicsTopicIdKpiKpiId: Endpoint[String, (String, String, EditKpiRequestDto), ApiError, Unit, Any] =
    endpoint.put
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "topics" / path[String]("topicId") / "kpi" / path[String]("kpiId"))
      .in(jsonBody[EditKpiRequestDto])
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.NoContent))
      .description("")

  val deleteApiAdminTopicsTopicIdKpiKpiId: Endpoint[String, (String, String), ApiError, Unit, Any] =
    endpoint.delete
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "topics" / path[String]("topicId") / "kpi" / path[String]("kpiId"))
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.NoContent))
      .description("")

}
