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
    getApiAdminGroups,
    putApiAdminGroupsGroupIdKpiKpiId,
    deleteApiAdminGroupsGroupIdKpiKpiId,
    putApiAdminGroupsGroupIdTeacherTeacherId,
    deleteApiAdminGroupsGroupIdTeacherTeacherId
  )

  def postApiAdminGroups: Endpoint[String, CreateGroupRequestDto, ApiError, Unit, Any] =
    baseEndpoint.post
      .tag("Groups API")
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "groups")
      .in(jsonBody[CreateGroupRequestDto])
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.NoContent))
      .description("Создание груп от роли админа")

  def getApiAdminGroups: Endpoint[String, Unit, ApiError, List[GroupResponseItemDto], Any] =
    baseEndpoint.get
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
      .description("Получения списков групп от роли админа")

  def putApiAdminGroupsGroupIdKpiKpiId: Endpoint[String, (String, String), ApiError, Unit, Any] =
    baseEndpoint.put
      .tag("Groups API")
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "groups" / path[String]("groupId") / "kpis" / path[String]("kpiId"))
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.NoContent))
      .description("Привязка показателей эффективности к группе")

  def deleteApiAdminGroupsGroupIdKpiKpiId: Endpoint[String, (String, String), ApiError, Unit, Any] =
    baseEndpoint.delete
      .tag("Groups API")
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "groups" / path[String]("groupId") / "kpis" / path[String]("kpiId"))
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.NoContent))
      .description("Отвязка показателей эффективности от группы")

  def putApiAdminGroupsGroupIdTeacherTeacherId: Endpoint[String, (String, String), ApiError, Unit, Any] =
    baseEndpoint.put
      .tag("Groups API")
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "groups" / path[String]("groupId") / "teacher" / path[String]("teacherId"))
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.NoContent))
      .description("Привязка учителя к группе")

  def deleteApiAdminGroupsGroupIdTeacherTeacherId: Endpoint[String, (String, String), ApiError, Unit, Any] =
    baseEndpoint.delete
      .tag("Groups API")
      .securityIn(auth.bearer[String]())
      .in("api" / "admin" / "groups" / path[String]("groupId") / "teacher" / path[String]("teacherId"))
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.NoContent))
      .description("Отвязка учителя от группы")


}
