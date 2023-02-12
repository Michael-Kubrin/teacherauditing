package org.sibadi.auditing.api.endpoints

import io.circe.generic.auto._
import org.sibadi.auditing.api.model.ApiError._
import org.sibadi.auditing.api.model._
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe.jsonBody

object ReviewerActionsAPI {

  def reviewerActionsApi = List(
    putApiAdminTopicsTopicIdKpiKpiIdTeachersTeacherIdStatus,
    getApiReviewerTeachers,
    getApiReviewerEstimate
  )

  def putApiAdminTopicsTopicIdKpiKpiIdTeachersTeacherIdStatus
    : Endpoint[String, (String, String, String, EditTeacherStatusRequest), ApiError, Unit, Any] =
    baseEndpoint.put
      .tag("Teacher Actions API")
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "topics" / path[String]("topicId") / "kpi" / path[String]("kpiId") / "teachers" / path[String]("teacherId") / "status")
      .in(jsonBody[EditTeacherStatusRequest])
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.NoContent))
      .description("Запрос на изменения статуса проверки")

  def getApiReviewerTeachers: Endpoint[String, String, ApiError, TeacherResponse, Any] =
    baseEndpoint.get
      .tag("Reviewer Actions API")
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "teachers" / path[String]("teacherId"))
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(jsonBody[TeacherResponse])
      .description("")

  def getApiReviewerEstimate: Endpoint[String, String, ApiError, EstimateResponse, Any] =
    baseEndpoint.get
      .tag("Reviewer Actions API")
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "teachers" / path[String]("teacherId"))
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(jsonBody[EstimateResponse])
      .description("")

}
