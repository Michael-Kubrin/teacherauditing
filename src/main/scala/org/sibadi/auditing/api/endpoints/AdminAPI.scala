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
    editTopicNameEndpoint,
    //kpi crud
    createKpiEndpoint,
    getAllKpiEndpoint,
    editKpiEndpoint,
    deleteKpiEndpoint,
    //teachers crud
    createTeachersEndpoint,
    getTeachersEndpoint,
    editTeachersEndpoint,
    deleteTeachersEndpoint,
    //reviewers crud
    createReviewersEndpoint,
    getAllReviewersEndpoint,
    editReviewersEndpoint,
    deleteReviewersEndpoint
  )

  def createGroupEndpoint: Endpoint[Unit, CreateGroupRequestDto, ApiError, Unit, Any] =
    baseEndpoint.post
      .tags(List("API Admin"))
      .in("groups")
      .summary("Создание группы")
      .description("Создание группы")
      .in(jsonBody[CreateGroupRequestDto].example(exampleCreateGroupRequestDto))
      .errorOut(
        oneOf[ApiError](
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
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.NoContent).description("Успешное удаление группы"))

  def editGroupEndpoint: Endpoint[Unit, (String, EditGroupName), ApiError, Unit, Any] =
    baseEndpoint.put
      .tag("API Admin")
      .in(
        "groups" / path[String]("groupId")
          .description("Идентификатор группы, которую необходимо изменить")
          .example("fhasdhf4327890fdshkl")
      )
      .summary("Изменения имени группы")
      .in(jsonBody[EditGroupName])
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.NoContent))

  def createTopicEndpoint: Endpoint[Unit, CreateTopicName, ApiError, Unit, Any] =
    baseEndpoint.post
      .tag("API Admin")
      .in("topics")
      .in(jsonBody[CreateTopicName].description("Название топика").example(CreateTopicName("Качество приема")))
      .description("Создание раздела")
      .summary("Создание раздела")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.unsafeApply(201)))

  def getAllTopicsEndpoint: Endpoint[Unit, Unit, ApiError, List[TopicItemResponseDto], Any] =
    baseEndpoint.get
      .tag("API Admin")
      .in("topics")
      .description("Получение всех разделов")
      .summary("Получение всех разделов")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(jsonBody[List[TopicItemResponseDto]])

  def deleteTopicEndpoint: Endpoint[Unit, String, ApiError, Unit, Any] =
    baseEndpoint.delete
      .tag("API Admin")
      .in("topics" / path[String]("topicId").description("Индентификатор топика").example("12345"))
      .description("Удаление раздела")
      .summary("Удаление раздела")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.NoContent))

  def editTopicNameEndpoint: Endpoint[Unit, (String, EditTopicRequestDto), ApiError, Unit, Any] =
    baseEndpoint.put
      .tag("API Admin")
      .in("topics" / path[String]("topicId").description("Индентификатор топика").example("12345"))
      .in(jsonBody[EditTopicRequestDto])
      .description("Внесение изменений в раздел")
      .summary("Внесение изменений в раздел")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.NoContent))

  def createKpiEndpoint: Endpoint[Unit, (String, CreateKPIRequestDto), ApiError, ResponseId, Any] =
    baseEndpoint.post
      .tag("API Admin")
      .in("topics" / path[String]("topicId").description("Идентификатор раздела").example("12345") / "kpi")
      .in(jsonBody[CreateKPIRequestDto])
      .description("Создание KPI. KPI - Ключевой Показатель эффективности")
      .summary("Создание *Ключевой Показатель эффективности*")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(jsonBody[ResponseId])

  def getAllKpiEndpoint: Endpoint[Unit, String, ApiError, List[TopicKpiResponse], Any] =
    baseEndpoint.get
      .tag("API Admin")
      .in("topics" / path[String]("topicId").description("Идентификатор раздела").example("12345") / "kpi")
      .description("Получения списка KPI. KPI - Ключевой Показатель эффективности")
      .summary("Получения списка *Ключевой Показатель эффективности*")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(jsonBody[List[TopicKpiResponse]])

  def editKpiEndpoint: Endpoint[Unit, (String, String, EditKpiRequestDto), ApiError, Unit, Any] =
    baseEndpoint.put
      .tag("API Admin")
      .in(
        "topics" / path[String]("topicId").description("Идентификатор раздела").example("12345") / "kpi" / path[String]("kpiId")
          .description("Идентификатор KPI")
          .example("678910")
      )
      .description("Изменения KPI. KPI - Ключевой Показатель эффективности")
      .summary("Изменения имени *Ключевой Показатель эффективности*")
      .in(jsonBody[EditKpiRequestDto])
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.NoContent))

  def deleteKpiEndpoint: Endpoint[Unit, (String, String), ApiError, Unit, Any] =
    baseEndpoint.delete
      .tag("API Admin")
      .in(
        "topics" / path[String]("topicId")
          .description("Идентификатор раздела")
          .example("12345") / "kpi" / path[String]("kpiId")
          .description("Идентификатор KPI")
          .example("678910")
      )
      .description("Удаления KPI. KPI - Ключевой Показатель эффективности")
      .summary("Удаления *Ключевой Показатель эффективности*")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.NoContent))

  def createTeachersEndpoint: Endpoint[Unit, CreateTeacherRequest, ApiError, ResponseIdPassword, Any] =
    baseEndpoint.post
      .tag("API Admin")
      .in("teachers")
      .in(
        jsonBody[CreateTeacherRequest]
          .description("Входные парамметры оцениваемого персонала: ФИО, логин")
          .example(CreateTeacherRequest("Иванов", "Иван", Some("Иванович"), "Ivan"))
      )
      .description("Создание оцениваемого персонала")
      .summary("Создание оцениваемого персонала")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(
        jsonBody[ResponseIdPassword]
          .description("Идентификатор и пароль на созданного пользователя")
          .example(ResponseIdPassword("ivan777", "fgjskdbgksjgbsglijks"))
      )

  def getTeachersEndpoint: Endpoint[Unit, Unit, ApiError, List[TeacherItemResponse], Any] =
    baseEndpoint.get
      .tag("API Admin")
      .in("teachers")
      .description("Получение списка оцениваемого персонала")
      .summary("Получение списка оцениваемого персонала")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(
        jsonBody[List[TeacherItemResponse]]
          .description("Список оцениваемого персонала с их данными: Идентификатор, ФИО")
          .example(List(TeacherItemResponse("ivan777", "Иванов", "Иван", Some("Иванович"))))
      )

  def editTeachersEndpoint: Endpoint[Unit, (String, EditTeacherRequest), ApiError, Unit, Any] =
    baseEndpoint.put
      .tag("API Admin")
      .in("teachers" / path[String]("teacherId").description("Идентификатор оценивающего персонала").example("1111s"))
      .in(
        jsonBody[EditTeacherRequest]
          .description("Данные конкретного оцениваемого персонала, которые необходимо изменить: ФИО")
          .example(EditTeacherRequest("Сергеев", "Сергей", Some("Сергеевич")))
      )
      .description("Изменение конкретного пользователя с ролью *Проверяющий*")
      .summary("Изменение конкретного пользователя с ролью *Проверяющий*")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .out(statusCode(StatusCode.NoContent))

  def deleteTeachersEndpoint: Endpoint[Unit, String, ApiError, Unit, Any] =
    baseEndpoint.put
      .tag("API Admin")
      .in("teachers" / path[String]("teacherId").description("Идентификатор оценивающего персонала").example("1111s"))
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .description("Удаление конкретного пользователя с ролью *Оцениваемый персонал*")
      .summary("Удаление конкретного пользователя с ролью *Оцениваемый персонал*")
      .out(statusCode(StatusCode.NoContent))

  def createReviewersEndpoint: Endpoint[Unit, CreateReviewerRequest, ApiError, ResponseIdPassword, Any] =
    baseEndpoint.post
      .tag("API Admin")
      .in("reviewers")
      .in(
        jsonBody[CreateReviewerRequest]
          .description("Входные парамметры проверяющего: ФИО, логин")
          .example(CreateReviewerRequest("Иванов", "Иван", Some("Иванович"), "Ivan"))
      )
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .description("Создание пользователя с ролью *Проверяющий*")
      .summary("Создание пользователя с ролью *Проверяющий*")
      .out(
        jsonBody[ResponseIdPassword]
          .description("Идентификатор и пароль на созданного пользователя")
          .example(ResponseIdPassword("ivan777", "fgjskdbgksjgbsglijks"))
      )

  def getAllReviewersEndpoint: Endpoint[Unit, Unit, ApiError, List[ReviewerResponse], Any] =
    baseEndpoint.get
      .tag("API Admin")
      .in("reviewers")
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .description("Получение списка всех пользователей с ролью *Проверяющий*")
      .summary("Получение списка всех пользователей с ролью *Проверяющий*")
      .out(
        jsonBody[List[ReviewerResponse]]
          .description("Список проверяющего персонала с их данными: Идентификатор, ФИО")
          .example(List(ReviewerResponse("ivan777", "Иванов", "Иван", Some("Иванович"))))
      )

  def editReviewersEndpoint: Endpoint[Unit, (String, EditReviewersRequest), ApiError, Unit, Any] =
    baseEndpoint.put
      .tag("API Admin")
      .in("reviewers" / path[String]("reviewerId").description("Идентификатор проверяемого персонала").example("2222c"))
      .in(
        jsonBody[EditReviewersRequest]
          .description("Данные конкретного проверяющего персонала, которые необходимо изменить: ФИО")
          .example(EditReviewersRequest("Сергеев", "Сергей", Some("Сергеевич")))
      )
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .description("Изменения конкретного пользователя с ролью *Проверяющий*")
      .summary("Изменения конкретного пользователя с ролью *Проверяющий*")
      .out(statusCode(StatusCode.NoContent))

  def deleteReviewersEndpoint: Endpoint[Unit, String, ApiError, Unit, Any] =
    baseEndpoint.put
      .tag("API Admin")
      .in("reviewers" / path[String]("reviewerId").description("Идентификатор проверяемого персонала").example("2222c"))
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description(""))),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )
      .description("Удаление конкретного пользователя с ролью *Проверяющий*")
      .summary("Удаление конкретного пользователя с ролью *Проверяющий*")
      .out(statusCode(StatusCode.NoContent))

  def putApiAdminGroupsGroupIdKpiKpiId: Endpoint[String, (String, String), ApiError, Unit, Any] =
    baseEndpoint.put
      .tag("API Admin")
      .securityIn(auth.bearer[String]())
      .in("groups" / path[String]("groupId") / "kpis" / path[String]("kpiId"))
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
      .in("groups" / path[String]("groupId") / "kpis" / path[String]("kpiId"))
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
      .in("groups" / path[String]("groupId") / "teacher" / path[String]("teacherId"))
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
      .in("groups" / path[String]("groupId") / "teacher" / path[String]("teacherId"))
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
      .in("groups" / path[String]("groupId") / "teacher" / path[String]("teacherId"))
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
