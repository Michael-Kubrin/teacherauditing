package org.sibadi.auditing.api.endpoints

import org.sibadi.auditing.api.model._
import ApiError._
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe.jsonBody
import io.circe.generic.auto._

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
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[NotFound].description("Server down")))
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
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[NotFound].description("Server down")))
        )
      )
      .out(jsonBody[List[GroupResponseItemDto]])
      .description("")

}
