package org.sibadi.auditing.api.endpoints

import org.sibadi.auditing.api.model._
import ApiError._
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe.jsonBody
import io.circe.generic.auto._

object KpiTeacherAPI {

  def kpiTeacherApi = List(
    postApiAdminTopicsTopicIdKpiKpiIdTeacherTeacherId,
    deleteApiAdminTopicsTopicIdKpiKpiIdTeacherTeacherId
  )

  def postApiAdminTopicsTopicIdKpiKpiIdTeacherTeacherId: Endpoint[String, (String, String, String), ApiError, Unit, Any] =
    endpoint.post
      .tag("Kpi-Teacher API")
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "topics" / path[String]("topicId") / "kpi" / path[String]("kpiId") / "teacher" / path[String]("teacherId"))
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.unsafeApply(201)))
      .description("")

  def deleteApiAdminTopicsTopicIdKpiKpiIdTeacherTeacherId: Endpoint[String, (String, String, String), ApiError, String, Any] =
    endpoint.delete
      .tag("Kpi-Teacher API")
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "topics" / path[String]("topicId") / "kpi" / path[String]("kpiId") / "teacher" / path[String]("teacherId"))
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
