package org.sibadi.auditing.api.endpoints

import org.sibadi.auditing.api.model.ApiError._
import org.sibadi.auditing.api.endpoints.Examples._
import org.sibadi.auditing.api.model._
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe.jsonBody
import io.circe.generic.auto._

object AdminAPI {

  // register teacher and crud
  // register reviewer and crud
  // kpi crud
  // link/unlink teacher to group
  // link/unlink kpi to group

  def all = List(
    // group crud
    createGroupEndpoint,
    getAllGroupsEndpoint,
    deleteGroupEndpoint,
    editGroupEndpoint,
    // topics crud
    createTopicEndpoint,
    getAllTopicsEndpoint,
    deleteTopicEndpoint,
    editTopicNameEndpoint
  )

  def createGroupEndpoint: Endpoint[String, CreateGroupRequestDto, ApiError, Unit, Any] =
    baseEndpoint.post
      .tag("API Admin")
      .tags(List("API Admin", "API REviewerfjksadljfklasdhjkfjkewahsjkfhjkas"))
      .securityIn(auth.bearer[String]())
      .in("groups")
      .summary("Создание группы")
      .description("Создание группы")
      .in(jsonBody[CreateGroupRequestDto].example(exampleCreateGroupRequestDto))
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description("Параметры запроса невалидны"))),
          oneOfVariant(
            statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description("Недостаточно прав для авторизованного пользователя"))
          ),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Критическая ошибка")))
        )
      )
      .out(statusCode(StatusCode.NoContent).description("Успешное создание группы"))

  def getAllGroupsEndpoint: Endpoint[Unit, Unit, ApiError, List[GroupResponseItemDto], Any] =
    baseEndpoint.get
      .tag("API Admin")
      .in("groups")
      .summary("Список групп")
      .description("Запрос возвращает список всех доступных групп, а также подвязанных к каждой группе показатели эффективности и участников группы")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(jsonBody[List[GroupResponseItemDto]].example(exampleGroupResponseItemDtoList))

  def deleteGroupEndpoint: Endpoint[Unit, String, ApiError, Unit, Any] =
    baseEndpoint.delete
      .tag("API Admin")
      .in("groups" / path[String]("groupId").description("Идентификатор группы, которую необходимо удалить").example("fhasdhf4327890fdshkl"))
      .summary("Удаление группы")
      .description("Запрос удаляет группу")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.NoContent).description("Успешное удаление группы"))

  def editGroupEndpoint: Endpoint[Unit, (String, EditGroupName), ApiError, Unit, Any] =
    baseEndpoint.put
      .tag("API Admin")
      .in(
        "api" / "admin" / "groups" / path[String]("groupId")
          .description("Идентификатор группы, которую необходимо изменить")
          .example("fhasdhf4327890fdshkl")
      )
      .in(jsonBody[EditGroupName])
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.NoContent))

  def createTopicEndpoint: Endpoint[Unit, CreateTopicName, ApiError, Unit, Any] =
    baseEndpoint.post
      .tag("Topics API")
      .in("api" / "admin" / "topics")
      .in(jsonBody[CreateTopicName].description("Название топика").example(CreateTopicName("Качество приема")))
      .description("Создание раздела")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.unsafeApply(201)))

  def getAllTopicsEndpoint: Endpoint[Unit, Unit, ApiError, List[TopicItemResponseDto], Any] =
    baseEndpoint.get
      .tag("Topics API")
      .in("api" / "admin" / "topics")
      .description("Получение всех разделов")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(jsonBody[List[TopicItemResponseDto]])

  def deleteTopicEndpoint: Endpoint[Unit, String, ApiError, Unit, Any] =
    baseEndpoint.delete
      .tag("Topics API")
      .in("api" / "admin" / "topics" / path[String]("topicId").description("Индентификатор топика").example("12345"))
      .description("Удаление раздела")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.NoContent))

  def editTopicNameEndpoint: Endpoint[Unit, (String, EditTopicRequestDto), ApiError, Unit, Any] =
    baseEndpoint.put
      .tag("Topics API")
      .in("api" / "admin" / "topics" / path[String]("topicId").description("Индентификатор топика").example("12345"))
      .in(jsonBody[EditTopicRequestDto])
      .description("Внесение изменений в раздел")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.NoContent))

  def putApiAdminGroupsGroupIdKpiKpiId: Endpoint[String, (String, String), ApiError, Unit, Any] =
    baseEndpoint.put
      .tag("API Admin")
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
      .tag("API Admin")
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
      .tag("API Admin")
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
      .tag("API Admin")
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

  def deleteApiAdminGroupsGroupId: Endpoint[String, (String, String), ApiError, Unit, Any] =
    baseEndpoint.delete
      .tag("API Admin")
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
