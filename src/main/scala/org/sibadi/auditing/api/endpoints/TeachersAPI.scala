package org.sibadi.auditing.api.endpoints

import org.sibadi.auditing.api.model._
import ApiError._
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe.jsonBody
import io.circe.generic.auto._

object TeachersAPI {

  def teachersApi = List(
    postApiAdminTeachers,
    getApiAdminTeachers,
    putApiAdminTeachers
  )

  def postApiAdminTeachers: Endpoint[String, CreateTeacherRequest, ApiError, ResponseIdPassword, Any] =
    endpoint.post
      .tag("Teachers API")
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "teachers")
      .in(jsonBody[CreateTeacherRequest])
      .description("")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(jsonBody[ResponseIdPassword])

  def getApiAdminTeachers: Endpoint[String, Unit, ApiError, List[TeacherResponse], Any] =
    endpoint.get
      .tag("Teachers API")
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "teachers")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(jsonBody[List[TeacherResponse]])
      .description("")
  def putApiAdminTeachers: Endpoint[String, (String, EditTeacherRequest), ApiError, Unit, Any] =
    endpoint.put
      .tag("Teachers API")
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "teachers" / path[String]("teacherId"))
      .in(jsonBody[EditTeacherRequest])
      .description("")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.NoContent))

}
