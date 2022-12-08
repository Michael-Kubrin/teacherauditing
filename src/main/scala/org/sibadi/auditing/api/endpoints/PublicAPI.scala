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
          oneOfVariant(statusCode(StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(401)).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(500)).and(jsonBody[InternalError].description("Server down")))
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
          oneOfVariant(statusCode(StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(401)).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(500)).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(jsonBody[PasswordResponse])

  val postApiPublicTopicsTopicIdKpiKpiIdFilesFileId: Endpoint[Unit, (String, String, String), ApiError, _root_.sttp.tapir.TapirFile, Any] =
    endpoint.get
      .in("api" / "public" / "topicsApi" / path[String]("topicId") / "kpi" / path[String]("kpiId") / "files" / path[String]("fileId"))
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(sttp.model.StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(fileBody)
      .description("")

}
