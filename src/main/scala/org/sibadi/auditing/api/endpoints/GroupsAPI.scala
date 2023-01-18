package org.sibadi.auditing.api.endpoints

import io.circe.generic.auto._
import org.sibadi.auditing.api.model.ApiError._
import org.sibadi.auditing.api.model._
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe.jsonBody

object GroupsAPI {

  def groupsApi = List(
    postApiAdminGroups,
    getApiAdminGroups
  )

  def postApiAdminGroups: Endpoint[String, CreateGroupRequestDtp, ApiError, Unit, Any] =
    endpoint.post
      .tag("Groups API")
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "groups")
      .in(jsonBody[CreateGroupRequestDtp])
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.unsafeApply(201)))
      .description("")

  def getApiAdminGroups: Endpoint[String, Unit, ApiError, List[GroupResponseItemDto], Any] =
    endpoint.get
      .tag("Groups API")
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "groups")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(jsonBody[List[GroupResponseItemDto]])
      .description("")

}
