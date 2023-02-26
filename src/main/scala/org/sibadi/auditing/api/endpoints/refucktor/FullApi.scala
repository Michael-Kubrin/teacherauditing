package org.sibadi.auditing.api.endpoints.refucktor

import io.circe.generic.auto._
import org.sibadi.auditing.api.endpoints.baseEndpoint
import org.sibadi.auditing.api.endpoints.refucktor.TapirErrors._
import org.sibadi.auditing.api.endpoints.refucktor.TapirExamples._
import org.sibadi.auditing.api.endpoints.refucktor.model._
import org.sibadi.auditing.api.model._
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._

object FullApi {

  val all = List(
    loginEndpoint,
    getKpiDataForTeacher,
    getKpisByTeacherForReviewer,
    estimateTeacherEndpoint,
    fillKpiEndpoint
  )

  def fillKpiEndpoint =
    baseEndpoint.post
      .tag("API Teacher")
      .summary("Занесение данных для оценки")
      .description("Занесение описания и файлов")
      .in("topics" / path[String]("topicId") / "kpi" / path[String]("kpiId"))
      .in(multipartBody[FillKpiRequestDto])
      .out(statusCode(StatusCode.NoContent))

  def estimateTeacherEndpoint =
    baseEndpoint.put
      .tag("API Reviewer")
      .summary("Оценивание эффективности")
      .in("teachers" / path[String]("teacherId") / "kpi" / path[String]("kpiId"))
      .in(jsonBody[EstimateTeacherRequestDto].example(EstimateTeacherRequestDto(0.99)))
      .out(statusCode(StatusCode.NoContent))
      .errorOut(oneOf[ApiError](badRequest400, unauthorized401, serverError500))

  def getKpisByTeacherForReviewer =
    baseEndpoint.get
      .tag("API Reviewer")
      .summary("Получение данных по оценке эффективности для ревьювера")
      .in("teachers" / path[String]("teacherId") / "kpi" / path[String]("kpiId"))
      .out(jsonBody[KpiReviewerDataResponseDto].example(kpiReviewerDataResponseDtoExample))
      .errorOut(oneOf[ApiError](badRequest400, unauthorized401, serverError500))

  def getKpiDataForTeacher =
    baseEndpoint.get
      .tags(List("API Teacher", "API Reviewer"))
      .summary("Данные по показателю эффективности")
      .description(
        "Получение данных по показателю эффективности, под которыми понимаются статус оценки, список прикреплённых файлов в случае заполнения, балл в случае ревью, а также описание"
      )
      .in("topics" / path[String]("topicId") / "kpis" / path[String]("kpiId"))
      .out(jsonBody[KpiDataResponseDto].example(kpiDataExample))
      .errorOut(oneOf[ApiError](badRequest400, unauthorized401, serverError500))

  def loginEndpoint =
    baseEndpoint.post
      .tags(List("API Reviewer", "API Teacher"))
      .summary("Авторизация")
      .description("Получение токена авторизации")
      .in("login")
      .in(jsonBody[LoginRequestDto].example(loginExample))
      .out(jsonBody[BearerResponseDto].example(bearerExample))
      .errorOut(oneOf[ApiError](badRequest400, unauthorized401, serverError500))

}
