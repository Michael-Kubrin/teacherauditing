package org.sibadi.auditing.api.endpoints.refucktor

import org.sibadi.auditing.api.model.ApiError._
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._

object TapirErrors {
  val badRequest400   = oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description("Невалидные параметры")))
  val unauthorized401 = oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description("Не авторизован")))
  val notFound404     = oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found")))
  val serverError500  = oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
}