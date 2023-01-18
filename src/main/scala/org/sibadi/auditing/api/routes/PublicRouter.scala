package org.sibadi.auditing.api.routes

import cats.effect.Sync
import cats.syntax.all._
import org.sibadi.auditing.api.endpoints.PublicAPI._
import org.sibadi.auditing.api.model.{ApiError, LoginResponse, PasswordResponse}
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

  private implicit def logger: Logger[F] = Slf4jLogger.getLogger

  def routes = List(createLogin, changePassword, publicUploadFileId)

  private def createLogin =
    postLogin
      .serverLogic { credentials =>
        authenticator.authenticate(credentials.login, credentials.password)
          .map(LoginResponse)
          .toRight(ApiError.Unauthorized("Unauthorized").cast)
          .value
          .handleErrorWith(throwableToUnexpected[F, LoginResponse])
      }

  private def changePassword =
    editPassword
      .serverSecurityLogic { token =>
        authenticator.atLeastTeacher(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value.handleErrorWith(throwableToUnexpected[F, Authenticator.UserType])
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[PasswordResponse].pure[F]
      }

  private def publicUploadFileId =
    postApiPublicTopicsTopicIdKpiKpiIdFilesFileId
      .serverSecurityLogic { token =>
        authenticator.atLeastTeacher(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value.handleErrorWith(throwableToUnexpected[F, Authenticator.UserType])
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
}
