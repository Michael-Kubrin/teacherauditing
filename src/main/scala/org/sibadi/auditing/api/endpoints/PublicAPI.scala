package org.sibadi.auditing.api.endpoints

import io.circe.generic.auto._
import org.sibadi.auditing.api.model.ApiError._
import org.sibadi.auditing.api.model._
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe.jsonBody

object PublicAPI {

  def publicApi = List(
    postLogin,
    editPassword,
    postApiPublicTopicsTopicIdKpiKpiIdFilesFileId,
    getPublicGroups,
    getApiPublicAllTopics
  )

  def postLogin: Endpoint[Unit, CreateAccountRequestDto, ApiError, LoginResponse, Any] =
    baseEndpoint.post
      .tag("Public API")
      .in("api" / "login")
      .in(jsonBody[CreateAccountRequestDto])
      .description("Создание логина")
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
    baseEndpoint.put
      .tag("Public API")
      .securityIn(auth.bearer[String]())
      .in("api" / "password")
      .description("Изменение пароля")
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
    baseEndpoint.get
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

  def getPublicGroups: Endpoint[String, Unit, ApiError, List[GroupResponseItemDto], Any] =
    baseEndpoint.get
      .tag("Public API")
      .securityIn(auth.bearer[String]())
      .in("api" / "public" / "groups")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(jsonBody[List[GroupResponseItemDto]])
      .description("Получения списков групп от роли админа")

  def getApiPublicAllTopics: Endpoint[String, Unit, ApiError, List[TopicItemResponseDto], Any] =
    baseEndpoint.get
      .tag("Teacher Actions API")
      .securityIn(auth.bearer[String]())
      .in("api" / "public" / "topics")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(jsonBody[List[TopicItemResponseDto]])
      .description("")

  def getApiPublicAllKpi: Endpoint[String, String, ApiError, List[TopicKpiResponse], Any] =
    baseEndpoint.get
      .tag("Kpi API")
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "topics" / path[String]("topicId") / "kpi")
      .description("Получения списка KPI. KPI - Ключевой Показатель эффективности")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(jsonBody[List[TopicKpiResponse]])

}
