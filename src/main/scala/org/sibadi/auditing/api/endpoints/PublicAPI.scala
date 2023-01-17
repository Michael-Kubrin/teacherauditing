package org.sibadi.auditing.api.endpoints

import org.sibadi.auditing.api.model._
import ApiError._
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe.jsonBody
import io.circe.generic.auto._

object PublicAPI {

  def publicApi = List(
    postLogin,
    editPassword,
    postApiPublicTopicsTopicIdKpiKpiIdFilesFileId
  )

  def postLogin: Endpoint[Unit, CreateAccountRequestDto, ApiError, LoginResponse, Any] =
    endpoint.post
      .tag("Public API")
      .in("api" / "login")
      .in(jsonBody[CreateAccountRequestDto])
      .description("")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(jsonBody[LoginResponse])

  def editPassword: Endpoint[String, ChangePasswordRequestDto, ApiError, PasswordResponse, Any] =
    endpoint.put
      .tag("Public API")
      .securityIn(auth.bearer[String]())
      .in("api" / "password")
      .description("")
      .in(jsonBody[ChangePasswordRequestDto])
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(jsonBody[PasswordResponse])

  def postApiPublicTopicsTopicIdKpiKpiIdFilesFileId: Endpoint[String, (String, String, String), ApiError, _root_.sttp.tapir.TapirFile, Any] =
    endpoint.get
      .tag("Public API")
      .securityIn(auth.bearer[String]())
      .in("api" / "public" / "topics" / path[String]("topicId") / "kpi" / path[String]("kpiId") / "files" / path[String]("fileId"))
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(fileBody)
      .description("")

}
