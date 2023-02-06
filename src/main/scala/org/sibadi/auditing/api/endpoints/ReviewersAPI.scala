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

  def postApiAdminReviewers: Endpoint[String, CreateReviewerRequest, ApiError, ResponseIdPassword, Any] =
    baseEndpoint.post
      .tag("Reviewers API")
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "reviewers")
      .in(jsonBody[CreateReviewerRequest])
      .description("Создание роли проверяющего")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(jsonBody[ResponseIdPassword])

  def getApiAdminReviewers: Endpoint[String, Unit, ApiError, List[ReviewerResponse], Any] =
    baseEndpoint.get
      .tag("Reviewers API")
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "reviewers")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(jsonBody[List[ReviewerResponse]])
      .description("Получение списка с ролью Проверяющий")

  def putApiAdminReviewersId: Endpoint[String, (String, EditReviewersRequest), ApiError, Unit, Any] =
    baseEndpoint.put
      .tag("Reviewers API")
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "reviewers" / path[String]("reviewerId"))
      .in(jsonBody[EditReviewersRequest])
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
