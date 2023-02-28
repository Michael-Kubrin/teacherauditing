package org.sibadi.auditing.api

import org.sibadi.auditing.api.endpoints.model.ApiError
import org.sibadi.auditing.api.endpoints.model.ApiError._
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.json.circe._
import sttp.tapir.generic.auto._

package object endpoints {

  val baseEndpoint = endpoint
    .out(header("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, PATCH, DELETE"))
    .out(header("Access-Control-Allow-Headers", "X-Requested-With, content-type"))
    .out(header("Access-Control-Allow-Credentials", "true"))
    .out(header("Access-Control-Allow-Origin", "*"))
    .errorOut(header("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, PATCH, DELETE"))
    .errorOut(header("Access-Control-Allow-Headers", "X-Requested-With, content-type"))
    .errorOut(header("Access-Control-Allow-Credentials", "true"))
    .errorOut(header("Access-Control-Allow-Origin", "*"))

  val badRequest400   = oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description("Невалидные параметры")))
  val unauthorized401 = oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description("Не авторизован")))
  val notFound404     = oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found")))
  val serverError500  = oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))

  val emptyNameError: ApiError = ApiError.BadRequest("empty name")
  val sameNameError: ApiError = ApiError.BadRequest("same name")
  val sqlError: ApiError       = ApiError.InternalError("sql error")
  val noGroupWithGivenId: ApiError       = ApiError.NotFound("no group with given id")
  val noKpiWithGivenId: ApiError       = ApiError.NotFound("no kpi with given id")
  val noTeacherWithGivenId: ApiError       = ApiError.NotFound("no teacher with given id")
  val unauthorized: ApiError       = ApiError.Unauthorized("unauthorized")

}
