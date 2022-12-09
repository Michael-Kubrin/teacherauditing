package org.sibadi.auditing.api.endpoints

import org.sibadi.auditing.api.model._
import ApiError._
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe.jsonBody
import io.circe.generic.auto._

object KpiGroupAPI {

  val kpiGroupApi = List(
    putApiAdminGroupsGroupIdTopicsTopicIdKpiKpiId,
    deleteApiAdminGroupsGroupIdTopicsTopicIdKpiKpiId
  )

  val putApiAdminGroupsGroupIdTopicsTopicIdKpiKpiId: Endpoint[String, (String, PutTeacherToGroupsRequest), ApiError, String, Any] =
    endpoint.put
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "teachers" / path[String]("teacherId") / "groups")
      .in(jsonBody[PutTeacherToGroupsRequest])
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.NoContent).and(stringBody))
      .description("")

  val deleteApiAdminGroupsGroupIdTopicsTopicIdKpiKpiId: Endpoint[String, (String, String), ApiError, String, Any] =
    endpoint.delete
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "teachers" / path[String]("teacherId") / "groups" / path[String]("groupId"))
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.NoContent).and(stringBody))
      .description("")

}
