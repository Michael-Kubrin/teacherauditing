package org.sibadi.auditing.api.endpoints

import org.sibadi.auditing.api.model._
import ApiError._
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe.jsonBody
import io.circe.generic.auto._

object TeachersAPI {

  val teachersApi = List(
    postApiAdminTeachers,
    getApiAdminTeachers,
    putApiAdminTeachers,
  )

  val postApiAdminTeachers: Endpoint[String, CreateTeacherRequest, ApiError, ResponseIdPassword, Any] =
    endpoint.post
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "teachers")
      .in(jsonBody[CreateTeacherRequest])
      .description("")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(jsonBody[ResponseIdPassword])

  val getApiAdminTeachers: Endpoint[String, Unit, ApiError, List[TeacherResponse], Any] =
    endpoint.get
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "teachers")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(jsonBody[List[TeacherResponse]])
      .description("")
  val putApiAdminTeachers: Endpoint[String, (String, EditTeacherRequest), ApiError, String, Any] =
    endpoint.put
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "teachers" / path[String]("teacherId"))
      .in(jsonBody[EditTeacherRequest])
      .description("")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.unsafeApply(204)).and(stringBody))

}
