package org.sibadi.auditing.api.endpoints

import org.sibadi.auditing.api.model._
import ApiError._
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe.jsonBody
import io.circe.generic.auto._

object PublicAPI {

  val publicApi = List(
    postLogin,
    editPassword,
    postApiPublicTopicsTopicIdKpiKpiIdFilesFileId
  )

  val postLogin: Endpoint[Unit, CreateAccountRequestDto, ApiError, LoginResponse, Any] =
    endpoint.post
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

  val editPassword: Endpoint[String, ChangePasswordRequestDto, ApiError, PasswordResponse, Any] =
    endpoint.put
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

  val postApiPublicTopicsTopicIdKpiKpiIdFilesFileId: Endpoint[String, (String, String, String), ApiError, _root_.sttp.tapir.TapirFile, Any] =
    endpoint.get
      .securityIn(auth.bearer[String]())
      .in("api" / "public" / "topics" / path[String]("topicId") / "kpi" / path[String]("kpiId") / "files" / path[String]("fileId"))
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(sttp.model.StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.Unauthorized).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(sttp.model.StatusCode.InternalServerError).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(fileBody)
      .description("")

}
