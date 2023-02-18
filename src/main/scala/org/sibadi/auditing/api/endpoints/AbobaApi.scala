package org.sibadi.auditing.api.endpoints

import org.sibadi.auditing.api.model.ApiError._
import org.sibadi.auditing.api.endpoints.Examples._
import org.sibadi.auditing.api.model._
import sttp.model.{Part, StatusCode}
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._
import io.circe.generic.auto._
import enumeratum._

import java.io.File

object AbobaApi {

  /*
  Teacher API

  POST /topics/:topicId/kpis/:kpiId [MULTIPART!!! https://tapir.softwaremill.com/en/latest/endpoint/forms.html]- занесение описания и файлов к оценке
   */

  val all = List(
    loginEndpoint,
    getKpiDataForTeacher,
    getKpisByTeacherForReviewer,
    estimateTeacherEndpoint
  )

  final case class FillKpiRequestDto(
    description: Part[String],
    files: List[Part[File]]
  )

  val a = MultipartCodec[FillKpiRequestDto]

  val fillKpiEndpoint =
    baseEndpoint
      .post
      .tag("API Teacher")
      .summary("Занесение данных для оценки")
      .description("Занесение описания и файлов ")
      .in("topics" / path[String]("topicId") / "kpi" / path[String]("kpiId"))
      .in(multipartBody[FillKpiRequestDto])





  final case class EstimateTeacherRequestDto(score: Double)

  val estimateTeacherEndpoint =
    baseEndpoint
      .put
      .tag("API Reviewer")
      .summary("Оценивание эффективности")
      .in("teachers" / path[String]("teacherId") / "kpi" / path[String]("kpiId"))
      .in(jsonBody[EstimateTeacherRequestDto].example(EstimateTeacherRequestDto(0.99)))
      .out(statusCode(StatusCode.NoContent))
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description("Невалидные параметры"))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description("Не авторизован"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )







  final case class KpiReviewerDataResponseDto(
    description: Option[String],
    score: Option[Double],
    files: List[Attachment],
    status: EstimateStatus
  )

  val kpiReviewerDataResponseDtoExample = KpiReviewerDataResponseDto(
    Some("Фотки, которые попросил декан, прикрепил"),
    None,
    List(attachmentExample),
    EstimateStatus.Filled
  )

  val getKpisByTeacherForReviewer =
    baseEndpoint
      .get
      .tag("API Reviewer")
      .summary("Получение данных по оценке эффективности для ревьювера")
      .in("teachers" / path[String]("teacherId") / "kpi" / path[String]("kpiId"))
      .out(jsonBody[KpiReviewerDataResponseDto].example(kpiReviewerDataResponseDtoExample))
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description("Невалидные параметры"))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description("Не авторизован"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )












  sealed trait EstimateStatus extends EnumEntry
  object EstimateStatus extends Enum[EstimateStatus] {
    val values = findValues
    case object WaitingForFill extends EstimateStatus
    case object Filled extends EstimateStatus
    case object Declined extends EstimateStatus
    case object Corrected extends EstimateStatus
    case object Accepted extends EstimateStatus
  }

  final case class Attachment(name: String, id: String)

  val attachmentExample = Attachment("фотки_с_корпоратива.zip", "jsaldh77t3giahkjfdndasmc")

  final case class KpiDataResponseDto(
    description: Option[String],
    score: Option[Double],
    files: List[Attachment],
    status: EstimateStatus
  )

  val kpiDataExample = KpiDataResponseDto(
    Some("Фотки, которые попросил декан, прикрепил"),
    Some(0.89),
    List(attachmentExample),
    EstimateStatus.Accepted
  )

  val getKpiDataForTeacher =
    baseEndpoint
      .get
      .tags(List("API Teacher", "API Reviewer"))
      .summary("Данные по показателю эффективности")
      .description("Получение данных по показателю эффективности, под которыми понимаются статус оценки, список прикреплённых файлов в случае заполнения, балл в случае ревью, а также описание")
      .in("topics" / path[String]("topicId") / "kpis" / path[String]("kpiId"))
      .out(jsonBody[KpiDataResponseDto].example(kpiDataExample))
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description("Невалидные параметры"))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description("Не авторизован"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )







  final case class LoginRequestDto(login: String, password: String)

  val loginExample = LoginRequestDto("aboba", "verysecret")

  final case class BearerResponseDto(bearer: String)

  val bearerExample = BearerResponseDto("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCc7szXqXKa+0hndaYFfSPNM2UKFvnSWj5bu85wNa3T+7Lb1BmddIt2hZNk0lcNb14J362FT5N27PabP4L8eZN7P9MJXicsQa9mmpbzQERKPorKtLmbDvTe0sNaX6eRBQ7yl87n+8CsYuH6M7Nqkw2/LghQWteoiosq5YTLK3dYKQIDAQAB")

  val loginEndpoint =
    baseEndpoint
      .post
      .tags(List("API Reviewer", "API Teacher"))
      .summary("Авторизация")
      .description("Получение токена авторизации")
      .in("login")
      .in(jsonBody[LoginRequestDto].example(loginExample))
      .out(jsonBody[BearerResponseDto].example(bearerExample))
      .errorOut(
        oneOf[ApiError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description("Невалидные параметры"))),
          oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description("Не авторизован"))),
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalError].description("Server down")))
        )
      )

}
