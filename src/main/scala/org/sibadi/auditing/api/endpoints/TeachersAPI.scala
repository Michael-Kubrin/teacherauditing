package org.sibadi.auditing.api.endpoints

import io.circe.generic.auto._
import org.sibadi.auditing.api.model.ApiError._
import org.sibadi.auditing.api.model._
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe.jsonBody

object TeachersAPI {

  def teachersApi = List(
    postApiAdminTeachers,
    getApiAdminTeachers,
    putApiAdminTeachers,
    getApiAdminTeachersTeacherId
  )

  def postApiAdminTeachers: Endpoint[String, CreateTeacherRequestDto, ApiError, CredentialsResponseDto, Any] =
    baseEndpoint.post
      .tag("Teachers API")
      .securityIn(auth.bearer[String]())
      .in("teachers")
      .in(jsonBody[CreateTeacherRequestDto])
      .description("")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(jsonBody[CredentialsResponseDto])

  def getApiAdminTeachers: Endpoint[String, Unit, ApiError, List[TeacherItemResponseDto], Any] =
    baseEndpoint.get
      .tag("Teachers API")
      .securityIn(auth.bearer[String]())
      .in("teachers")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(jsonBody[List[TeacherItemResponseDto]])
      .description("")

  def getApiAdminTeachersTeacherId: Endpoint[String, String, ApiError, TeacherResponse, Any] =
    baseEndpoint.get
      .tag("Teachers API")
      .securityIn(auth.bearer[String]())
      .in("teachers" / path[String]("teacherId"))
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

  def putApiAdminTeachers: Endpoint[String, (String, EditTeacherRequestDto), ApiError, Unit, Any] =
    baseEndpoint.put
      .tag("Teachers API")
      .securityIn(auth.bearer[String]())
      .in("teachers" / path[String]("teacherId"))
      .in(jsonBody[EditTeacherRequestDto])
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
