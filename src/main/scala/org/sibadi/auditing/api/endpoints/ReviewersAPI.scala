package org.sibadi.auditing.api.endpoints

import org.sibadi.auditing.api.model._
import ApiError._
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe.jsonBody
import io.circe.generic.auto._

object ReviewersAPI {

  val reviewersApi = List(
    postApiAdminReviewers,
    getApiAdminReviewers,
    putApiAdminReviewersId,
  )

  val postApiAdminReviewers: Endpoint[String, CreateReviewerRequest, ApiError, ResponseIdPassword, Any] =
    endpoint.post
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "reviewers")
      .in(jsonBody[CreateReviewerRequest])
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

  val getApiAdminReviewers: Endpoint[String, Unit, ApiError, List[ReviewerResponse], Any] =
    endpoint.get
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "reviewers")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.unsafeApply(400)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(401)).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(404)).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.unsafeApply(500)).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(jsonBody[List[ReviewerResponse]])
      .description("")

  val putApiAdminReviewersId: Endpoint[String, (String, EditReviewersRequest), ApiError, String, Any] =
    endpoint.put
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "reviewers" / path[String]("reviewerId"))
      .in(jsonBody[EditReviewersRequest])
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
