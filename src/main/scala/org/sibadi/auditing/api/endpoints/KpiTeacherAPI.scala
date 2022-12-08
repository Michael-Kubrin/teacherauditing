package org.sibadi.auditing.api.endpoints

import org.sibadi.auditing.api.model._
import ApiError._
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe.jsonBody
import io.circe.generic.auto._

object KpiTeacherAPI {

  val kpiTeacherApi = List(
    postApiAdminTopicsTopicIdKpiKpiIdTeacherTeacherId,
    deleteApiAdminTopicsTopicIdKpiKpiIdTeacherTeacherId
  )

  val postApiAdminTopicsTopicIdKpiKpiIdTeacherTeacherId: Endpoint[String, (String, String, String), ApiError, String, Any] =
    endpoint.post
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "topicsApi" / path[String]("topicId") / "kpi" / path[String]("kpiId") / "teacher" / path[String]("teacherId"))
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.unsafeApply(201)).and(stringBody))
      .description("")

  val deleteApiAdminTopicsTopicIdKpiKpiIdTeacherTeacherId: Endpoint[String, (String, String, String), ApiError, String, Any] =
    endpoint.delete
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "topicsApi" / path[String]("topicId") / "kpi" / path[String]("kpiId") / "teacher" / path[String]("teacherId"))
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.unsafeApply(204)).and(stringBody))
      .description("")

}
