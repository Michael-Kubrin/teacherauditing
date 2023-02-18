package org.sibadi.auditing.api.routes

import cats.data.EitherT
import cats.effect.Sync
import cats.syntax.all._
import org.sibadi.auditing.api.endpoints.PublicAPI._
import org.sibadi.auditing.api.model.{
  toApiError,
  ApiError,
  GroupResponseItemDto,
  KpiInGroupItemDto,
  LoginResponse,
  PasswordResponse,
  TeacherInGroupItemDto,
  TopicItemResponseDto,
  TopicKpiResponse
}
import org.sibadi.auditing.domain.errors.AppError
import org.sibadi.auditing.service.Authenticator.UserType
import org.sibadi.auditing.service._
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import sttp.tapir.model.ServerRequest
import sttp.tapir.{Defaults, TapirFile}

class PublicRouter[F[_]: Sync](
  authenticator: Authenticator[F],
  estimateService: EstimateService[F],
  groupService: GroupService[F],
  kpiService: KpiService[F],
  reviewerService: ReviewerService[F],
  teacherService: TeacherService[F],
  topicService: TopicService[F]
) {

  implicit private def logger: Logger[F] = Slf4jLogger.getLogger

  def routes = List(createLogin, changePassword, publicUploadFileId, getGroups, publicGetTopics, publicGetAllKpi)

  private def createLogin =
    postLogin
      .serverLogic { credentials =>
        authenticator
          .authenticate(credentials.login, credentials.password)
          .map(LoginResponse)
          .toRight(ApiError.Unauthorized("Unauthorized").cast)
          .value
          .handleErrorWith(throwableToUnexpected[F, LoginResponse])
      }

  private def changePassword =
    editPassword
      .serverSecurityLogic { token =>
        authenticator
          .atLeastTeacher(token)
          .toRight(ApiError.Unauthorized("Unauthorized").cast)
          .value
          .handleErrorWith(throwableToUnexpected[F, Authenticator.UserType])
      }
      .serverLogic { userType => body =>
        val logic: EitherT[F, AppError, String] = userType match {
          case UserType.Teacher(id) =>
            teacherService.changePassword(id, body.oldPassword, body.NewPassword)
          case UserType.Reviewer(id) =>
            reviewerService.changePassword(id, body.oldPassword, body.NewPassword)
          case UserType.Admin(_) =>
            EitherT.leftT(AppError.AdminCantChangePassword().cast)
        }
        logic
          .map(PasswordResponse)
          .leftSemiflatMap(toApiError[F])
          .value
          .handleErrorWith(throwableToUnexpected[F, PasswordResponse])
      }

  private def publicUploadFileId =
    postApiPublicTopicsTopicIdKpiKpiIdFilesFileId
      .serverSecurityLogic { token =>
        authenticator
          .atLeastTeacher(token)
          .toRight(ApiError.Unauthorized("Unauthorized").cast)
          .value
          .handleErrorWith(throwableToUnexpected[F, Authenticator.UserType])
      }
      .serverLogic { userType => body =>
//        estimateService
//          .createEstimateFiles(body._1, body._2, body._3, defaultCreateFile)
//          .leftSemiflatMap(toApiError[F])
//          .map(x => TapirFile)
//          .value.handleErrorWith(throwableToUnexpected[F, Unit])
        ApiError.InternalError("Not implemented").cast.asLeft[TapirFile].pure[F]
      }
  def defaultCreateFile[F[_]](implicit sync: Sync[F]): ServerRequest => F[TapirFile] = _ => sync.blocking(Defaults.createTempFile())

  // https://github.com/softwaremill/tapir/blob/master/server/http4s-server/src/main/scala/sttp/tapir/server/http4s/Http4sServerOptions.scala

  private def getGroups =
    getPublicGroups
      .serverSecurityLogic { token =>
        authenticator
          .atLeastTeacher(token)
          .toRight(ApiError.Unauthorized("Unauthorized").cast)
          .value
          .handleErrorWith(throwableToUnexpected[F, Authenticator.UserType])
      }
      .serverLogic { _ => _ =>
        groupService.getAllGroups
          .leftSemiflatMap(toApiError[F])
          .map(_.map { group =>
            val kpis     = group.kpis.map(kpi => KpiInGroupItemDto(kpi.id, kpi.title))
            val teachers = group.teachers.map(teacher => TeacherInGroupItemDto(teacher.id, teacher.firstName, teacher.lastName, teacher.middleName))
            GroupResponseItemDto(group.id, group.title, kpis, teachers)
          })
          .value
          .handleErrorWith(throwableToUnexpected[F, List[GroupResponseItemDto]])
      }

  private def publicGetTopics =
    getApiPublicAllTopics
      .serverSecurityLogic { token =>
        authenticator
          .atLeastTeacher(token)
          .toRight(ApiError.Unauthorized("Unauthorized").cast)
          .value
          .handleErrorWith(throwableToUnexpected[F, Authenticator.UserType])
      }
      .serverLogic { userType => body =>
        topicService.getAllTopics
          .leftSemiflatMap(toApiError[F])
          .map(_.map(topic => TopicItemResponseDto(topic.id, topic.title, List.empty)))
          .value
          .handleErrorWith(throwableToUnexpected[F, List[org.sibadi.auditing.api.model.TopicItemResponseDto]])
      }

  private def publicGetAllKpi =
    getApiPublicAllKpi
      .serverSecurityLogic { token =>
        authenticator
          .atLeastReviewer(token)
          .toRight(ApiError.Unauthorized("Unauthorized").cast)
          .value
          .handleErrorWith(throwableToUnexpected[F, Authenticator.UserType])
      }
      .serverLogic { userType => body =>
        kpiService.getAllKpi
          .leftSemiflatMap(toApiError[F])
          .map(_.map(x => TopicKpiResponse(x.id, x.title)))
          .value
          .handleErrorWith(throwableToUnexpected[F, List[org.sibadi.auditing.api.model.TopicKpiResponse]])
      }
}
