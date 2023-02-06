package org.sibadi.auditing.api.endpoints

import io.circe.generic.auto._
import org.sibadi.auditing.api.model.ApiError._
import org.sibadi.auditing.api.model._
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe.jsonBody

object KpiAPI {

  def kpiApi = List(
    postApiAdminTopicsTopicIdKpi,
    getApiAdminTopicsTopicIdKpi,
    putApiAdminTopicsTopicIdKpiKpiId,
    deleteApiAdminTopicsTopicIdKpiKpiId
  )

  def postApiAdminTopicsTopicIdKpi: Endpoint[String, (String, CreateKPIRequestDto), ApiError, ResponseId, Any] =
    baseEndpoint.post
      .tag("Kpi API")
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "topics" / path[String]("topicId") / "kpi")
      .in(jsonBody[CreateKPIRequestDto])
      .description("Создание KPI. KPI - Ключевой Показатель эффективности")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(jsonBody[ResponseId])

  def getApiAdminTopicsTopicIdKpi: Endpoint[String, String, ApiError, List[TopicKpiResponse], Any] =
    baseEndpoint.get
      .tag("Kpi API")
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "topics" / path[String]("topicId") / "kpi")
      .description("Получения списка KPI. KPI - Ключевой Показатель эффективности")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(jsonBody[List[TopicKpiResponse]])

  def putApiAdminTopicsTopicIdKpiKpiId: Endpoint[String, (String, String, EditKpiRequestDto), ApiError, Unit, Any] =
    baseEndpoint.put
      .tag("Kpi API")
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "topics" / path[String]("topicId") / "kpi" / path[String]("kpiId"))
      .in(jsonBody[EditKpiRequestDto])
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.NoContent))
      .description("Изменения KPI. KPI - Ключевой Показатель эффективности")

  def deleteApiAdminTopicsTopicIdKpiKpiId: Endpoint[String, (String, String), ApiError, Unit, Any] =
    baseEndpoint.delete
      .tag("Kpi API")
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "topics" / path[String]("topicId") / "kpi" / path[String]("kpiId"))
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.NoContent))
      .description("Удаления KPI. KPI - Ключевой Показатель эффективности")

}
