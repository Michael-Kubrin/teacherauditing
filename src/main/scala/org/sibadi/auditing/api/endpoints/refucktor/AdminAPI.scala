package org.sibadi.auditing.api.endpoints.refucktor

import io.circe.generic.auto._
import org.sibadi.auditing.api.endpoints.Examples._
import org.sibadi.auditing.api.endpoints.baseEndpoint
import org.sibadi.auditing.api.endpoints.refucktor.TapirErrors._
import org.sibadi.auditing.api.endpoints.refucktor.model._
import org.sibadi.auditing.api.model._
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe.jsonBody

object AdminAPI {

  def all = List(
    // group crud
    createGroupEndpoint,
    getAllGroupsEndpoint,
    deleteGroupEndpoint,
    editGroupEndpoint,
    // link/unlink teacher to group
    putApiAdminGroupsGroupIdTeacherTeacherId,
    deleteApiAdminGroupsGroupIdTeacherTeacherId,
    // link/unlink kpi to group
    putApiAdminGroupsGroupIdKpiKpiId,
    deleteApiAdminGroupsGroupIdKpiKpiId,
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
    deleteReviewersEndpoint,
  )

  def createGroupEndpoint =
    baseEndpoint.post
      .tags(List("API Admin"))
      .in("groups")
      .summary("Создание группы")
      .description("Создание группы")
      .in(jsonBody[CreateGroupRequestDto].example(exampleCreateGroupRequestDto))
      .errorOut(oneOf[ApiError](serverError500))
      .out(statusCode(StatusCode.NoContent).description("Успешное создание группы"))

  def getAllGroupsEndpoint =
    baseEndpoint.get
      .tag("API Admin")
      .in("groups")
      .summary("Список групп")
      .description("Запрос возвращает список всех доступных групп, а также подвязанных к каждой группе показатели эффективности и участников группы")
      .errorOut(oneOf[ApiError](badRequest400, serverError500))
      .out(jsonBody[List[GroupItemResponseDto]].example(exampleGroupResponseItemDtoList))

  def deleteGroupEndpoint =
    baseEndpoint.delete
      .tag("API Admin")
      .in("groups" / path[String]("groupId").description("Идентификатор группы, которую необходимо удалить").example("fhasdhf4327890fdshkl"))
      .summary("Удаление группы")
      .description("Запрос удаляет группу")
      .errorOut(oneOf[ApiError](badRequest400, notFound404, serverError500))
      .out(statusCode(StatusCode.NoContent).description("Успешное удаление группы"))

  def editGroupEndpoint =
    baseEndpoint.put
      .tag("API Admin")
      .in(
        "groups" / path[String]("groupId")
          .description("Идентификатор группы, которую необходимо изменить")
          .example("fhasdhf4327890fdshkl")
      )
      .summary("Изменения имени группы")
      .in(jsonBody[EditGroupRequestDto])
      .errorOut(oneOf[ApiError](badRequest400, notFound404, serverError500))
      .out(statusCode(StatusCode.NoContent))

  def createTopicEndpoint =
    baseEndpoint.post
      .tag("API Admin")
      .in("topics")
      .in(jsonBody[CreateTopicRequestDto].description("Название топика").example(CreateTopicRequestDto("Качество приема")))
      .description("Создание раздела")
      .summary("Создание раздела")
      .errorOut(oneOf[ApiError](badRequest400, notFound404, serverError500))
      .out(statusCode(StatusCode.unsafeApply(201)))

  def getAllTopicsEndpoint =
    baseEndpoint.get
      .tag("API Admin")
      .in("topics")
      .description("Получение всех разделов")
      .summary("Получение всех разделов")
      .errorOut(oneOf[ApiError](badRequest400, notFound404, serverError500))
      .out(jsonBody[List[TopicItemResponseDto]])

  def deleteTopicEndpoint =
    baseEndpoint.delete
      .tag("API Admin")
      .in("topics" / path[String]("topicId").description("Индентификатор топика").example("12345"))
      .description("Удаление раздела")
      .summary("Удаление раздела")
      .errorOut(oneOf[ApiError](badRequest400, notFound404, serverError500))
      .out(statusCode(StatusCode.NoContent))

  def editTopicNameEndpoint =
    baseEndpoint.put
      .tag("API Admin")
      .in("topics" / path[String]("topicId").description("Индентификатор топика").example("12345"))
      .in(jsonBody[EditTopicRequestDto])
      .description("Внесение изменений в раздел")
      .summary("Внесение изменений в раздел")
      .errorOut(oneOf[ApiError](badRequest400, notFound404, serverError500))
      .out(statusCode(StatusCode.NoContent))

  def createKpiEndpoint =
    baseEndpoint.post
      .tag("API Admin")
      .in("topics" / path[String]("topicId").description("Идентификатор раздела").example("12345") / "kpi")
      .in(jsonBody[CreateKPIRequestDto])
      .description("Создание KPI. KPI - Ключевой Показатель эффективности")
      .summary("Создание *Ключевой Показатель эффективности*")
      .errorOut(oneOf[ApiError](notFound404, serverError500))
      .out(jsonBody[IdResponseDto])

  def getAllKpiEndpoint =
    baseEndpoint.get
      .tag("API Admin")
      .in("topics" / path[String]("topicId").description("Идентификатор раздела").example("12345") / "kpi")
      .description("Получения списка KPI. KPI - Ключевой Показатель эффективности")
      .summary("Получения списка *Ключевой Показатель эффективности*")
      .errorOut(oneOf[ApiError](notFound404, serverError500))
      .out(jsonBody[List[TopicKpiItemResponseDto]])

  def editKpiEndpoint =
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
      .errorOut(oneOf[ApiError](notFound404, serverError500))
      .out(statusCode(StatusCode.NoContent))

  def deleteKpiEndpoint =
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
      .errorOut(oneOf[ApiError](notFound404, serverError500))
      .out(statusCode(StatusCode.NoContent))

  def createTeachersEndpoint =
    baseEndpoint.post
      .tag("API Admin")
      .in("teachers")
      .in(
        jsonBody[CreateTeacherRequestDto]
          .description("Входные парамметры оцениваемого персонала: ФИО, логин")
          .example(CreateTeacherRequestDto("Иванов", "Иван", Some("Иванович"), "Ivan"))
      )
      .description("Создание оцениваемого персонала")
      .summary("Создание оцениваемого персонала")
      .errorOut(oneOf[ApiError](notFound404, serverError500))
      .out(
        jsonBody[CredentialsResponseDto]
          .description("Идентификатор и пароль на созданного пользователя")
          .example(CredentialsResponseDto("ivan777", "fgjskdbgksjgbsglijks"))
      )

  def getTeachersEndpoint =
    baseEndpoint.get
      .tag("API Admin")
      .in("teachers")
      .description("Получение списка оцениваемого персонала")
      .summary("Получение списка оцениваемого персонала")
      .errorOut(oneOf[ApiError](badRequest400, notFound404, serverError500))
      .out(
        jsonBody[List[TeacherItemResponseDto]]
          .description("Список оцениваемого персонала с их данными: Идентификатор, ФИО")
          .example(List(TeacherItemResponseDto("ivan777", "Иванов", "Иван", Some("Иванович"))))
      )

  def editTeachersEndpoint =
    baseEndpoint.put
      .tag("API Admin")
      .in("teachers" / path[String]("teacherId").description("Идентификатор оценивающего персонала").example("1111s"))
      .in(
        jsonBody[EditTeacherRequestDto]
          .description("Данные конкретного оцениваемого персонала, которые необходимо изменить: ФИО")
          .example(EditTeacherRequestDto("Сергеев", "Сергей", Some("Сергеевич")))
      )
      .description("Изменение конкретного пользователя с ролью *Проверяющий*")
      .summary("Изменение конкретного пользователя с ролью *Проверяющий*")
      .errorOut(oneOf[ApiError](badRequest400, notFound404, serverError500))
      .out(statusCode(StatusCode.NoContent))

  def deleteTeachersEndpoint =
    baseEndpoint.delete
      .tag("API Admin")
      .in("teachers" / path[String]("teacherId").description("Идентификатор оценивающего персонала").example("1111s"))
      .errorOut(oneOf[ApiError](badRequest400, notFound404, serverError500))
      .description("Удаление конкретного пользователя с ролью *Оцениваемый персонал*")
      .summary("Удаление конкретного пользователя с ролью *Оцениваемый персонал*")
      .out(statusCode(StatusCode.NoContent))

  def createReviewersEndpoint =
    baseEndpoint.post
      .tag("API Admin")
      .in("reviewers")
      .in(
        jsonBody[CreateReviewerRequestDto]
          .description("Входные парамметры проверяющего: ФИО, логин")
          .example(CreateReviewerRequestDto("Иванов", "Иван", Some("Иванович"), "Ivan"))
      )
      .errorOut(oneOf[ApiError](badRequest400, notFound404, serverError500))
      .description("Создание пользователя с ролью *Проверяющий*")
      .summary("Создание пользователя с ролью *Проверяющий*")
      .out(
        jsonBody[CredentialsResponseDto]
          .description("Идентификатор и пароль на созданного пользователя")
          .example(CredentialsResponseDto("ivan777", "fgjskdbgksjgbsglijks"))
      )

  def getAllReviewersEndpoint =
    baseEndpoint.get
      .tag("API Admin")
      .in("reviewers")
      .errorOut(oneOf[ApiError](badRequest400, notFound404, serverError500))
      .description("Получение списка всех пользователей с ролью *Проверяющий*")
      .summary("Получение списка всех пользователей с ролью *Проверяющий*")
      .out(
        jsonBody[List[ReviewerItemResponseDto]]
          .description("Список проверяющего персонала с их данными: Идентификатор, ФИО")
          .example(List(ReviewerItemResponseDto("ivan777", "Иванов", "Иван", Some("Иванович"))))
      )

  def editReviewersEndpoint =
    baseEndpoint.put
      .tag("API Admin")
      .in("reviewers" / path[String]("reviewerId").description("Идентификатор проверяемого персонала").example("2222c"))
      .in(
        jsonBody[EditReviewerRequestDto]
          .description("Данные конкретного проверяющего персонала, которые необходимо изменить: ФИО")
          .example(EditReviewerRequestDto("Сергеев", "Сергей", Some("Сергеевич")))
      )
      .errorOut(oneOf[ApiError](badRequest400, notFound404, serverError500))
      .description("Изменения конкретного пользователя с ролью *Проверяющий*")
      .summary("Изменения конкретного пользователя с ролью *Проверяющий*")
      .out(statusCode(StatusCode.NoContent))

  def deleteReviewersEndpoint =
    baseEndpoint.delete
      .tag("API Admin")
      .in("reviewers" / path[String]("reviewerId").description("Идентификатор проверяемого персонала").example("2222c"))
      .errorOut(oneOf[ApiError](badRequest400, notFound404, serverError500))
      .description("Удаление конкретного пользователя с ролью *Проверяющий*")
      .summary("Удаление конкретного пользователя с ролью *Проверяющий*")
      .out(statusCode(StatusCode.NoContent))

  def putApiAdminGroupsGroupIdKpiKpiId =
    baseEndpoint.put
      .tag("API Admin")
      .securityIn(auth.bearer[String]())
      .in("groups" / path[String]("groupId") / "kpis" / path[String]("kpiId"))
      .errorOut(oneOf[ApiError](badRequest400, unauthorized401, notFound404, serverError500))
      .out(statusCode(StatusCode.NoContent))
      .description("Привязка показателей эффективности к группе")

  def deleteApiAdminGroupsGroupIdKpiKpiId =
    baseEndpoint.delete
      .tag("API Admin")
      .securityIn(auth.bearer[String]())
      .in("groups" / path[String]("groupId") / "kpis" / path[String]("kpiId"))
      .errorOut(oneOf[ApiError](badRequest400, unauthorized401, notFound404, serverError500))
      .out(statusCode(StatusCode.NoContent))
      .description("Отвязка показателей эффективности от группы")

  def putApiAdminGroupsGroupIdTeacherTeacherId =
    baseEndpoint.put
      .tag("API Admin")
      .securityIn(auth.bearer[String]())
      .in("groups" / path[String]("groupId") / "teacher" / path[String]("teacherId"))
      .errorOut(oneOf[ApiError](badRequest400, unauthorized401, notFound404, serverError500))
      .out(statusCode(StatusCode.NoContent))
      .description("Привязка учителя к группе")

  def deleteApiAdminGroupsGroupIdTeacherTeacherId =
    baseEndpoint.delete
      .tag("API Admin")
      .securityIn(auth.bearer[String]())
      .in("groups" / path[String]("groupId") / "teacher" / path[String]("teacherId"))
      .errorOut(oneOf[ApiError](badRequest400, unauthorized401, notFound404, serverError500))
      .out(statusCode(StatusCode.NoContent))
      .description("Отвязка учителя от группы")

}
