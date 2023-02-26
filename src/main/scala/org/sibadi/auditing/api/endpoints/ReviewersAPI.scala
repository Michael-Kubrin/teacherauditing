package org.sibadi.auditing.api.endpoints

import io.circe.generic.auto._
import org.sibadi.auditing.api.model.ApiError._
import org.sibadi.auditing.api.model._
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe.jsonBody

object ReviewersAPI {

  def reviewersApi = List(
    postApiAdminReviewers,
    getApiAdminReviewers,
    putApiAdminReviewersId
  )

  def postApiAdminReviewers: Endpoint[String, CreateReviewerRequestDto, ApiError, CredentialsResponseDto, Any] =
    baseEndpoint.post
      .tag("Reviewers API")
      .securityIn(auth.bearer[String]())
      .in("reviewers")
      .in(jsonBody[CreateReviewerRequestDto])
      .description("Создание роли проверяющего")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(jsonBody[CredentialsResponseDto])

  def getApiAdminReviewers: Endpoint[String, Unit, ApiError, List[ReviewerItemResponseDto], Any] =
    baseEndpoint.get
      .tag("Reviewers API")
      .securityIn(auth.bearer[String]())
      .in("reviewers")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(jsonBody[List[ReviewerItemResponseDto]])
      .description("Получение списка с ролью Проверяющий")

  def putApiAdminReviewersId: Endpoint[String, (String, EditReviewerRequestDto), ApiError, Unit, Any] =
    baseEndpoint.put
      .tag("Reviewers API")
      .securityIn(auth.bearer[String]())
      .in("reviewers" / path[String]("reviewerId"))
      .in(jsonBody[EditReviewerRequestDto])
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.NoContent))
      .description("Изменения в роли Проверяющий")

}
