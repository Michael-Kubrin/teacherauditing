package org.sibadi.auditing.api.endpoints

import org.sibadi.auditing.api.model.ApiError._
import org.sibadi.auditing.api.model._
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe.jsonBody

object KpiGroupAPI {

  def kpiGroupApi = List(
    putApiAdminGroupsGroupIdTopicsTopicIdKpiKpiId,
    deleteApiAdminGroupsGroupIdTopicsTopicIdKpiKpiId
  )

  def putApiAdminGroupsGroupIdTopicsTopicIdKpiKpiId: Endpoint[String, (String, String), ApiError, String, Any] =
    endpoint.put
      .tag("Kpi-Group API")
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "groups" / path[String]("groupId") / "kpis" / path[String]("kpiId"))
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.NoContent).and(stringBody))
      .description("")

  def deleteApiAdminGroupsGroupIdTopicsTopicIdKpiKpiId: Endpoint[String, (String, String), ApiError, String, Any] =
    endpoint.delete
      .tag("Kpi-Group API")
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "teachers" / path[String]("teacherId") / "groups" / path[String]("groupId"))
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.NoContent).and(stringBody))
      .description("")

}
