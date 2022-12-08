package org.sibadi.auditing.api.endpoints

import org.sibadi.auditing.api.model._
import ApiError._
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe.jsonBody
import io.circe.generic.auto._

object ReviewerActionsAPI {

  val reviewerActionsApi = List(
    putApiAdminTopicsTopicIdKpiKpiIdTeachersTeacherIdStatus
  )

  val putApiAdminTopicsTopicIdKpiKpiIdTeachersTeacherIdStatus: Endpoint[String, (String, String, String, EditTeacherStatusRequest), ApiError, String, Any] =
    endpoint.put
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "topicsApi" / path[String]("topicId") / "kpi" / path[String]("kpiId") / "teachers" / path[String]("teacherId") / "status")
      .in(jsonBody[EditTeacherStatusRequest])
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
