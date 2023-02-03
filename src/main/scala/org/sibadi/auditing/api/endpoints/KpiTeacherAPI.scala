package org.sibadi.auditing.api.endpoints

import org.sibadi.auditing.api.model.ApiError._
import org.sibadi.auditing.api.model._
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe.jsonBody

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
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.unsafeApply(201)))
      .description("Создание KPI для преподавателей. KPI - Ключевой Показатель эффективности")

  def deleteApiAdminTopicsTopicIdKpiKpiIdTeacherTeacherId: Endpoint[String, (String, String, String), ApiError, String, Any] =
    endpoint.delete
      .tag("Kpi-Teacher API")
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "topics" / path[String]("topicId") / "kpi" / path[String]("kpiId") / "teacher" / path[String]("teacherId"))
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.NoContent).and(stringBody))
      .description("Удаление KPI для преподавателей. KPI - Ключевой Показатель эффективности")

}
