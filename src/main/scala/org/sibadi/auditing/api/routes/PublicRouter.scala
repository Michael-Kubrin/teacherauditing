package org.sibadi.auditing.api.routes

import cats.Monad
import cats.effect.Sync
import cats.syntax.applicative._
import cats.syntax.either._
import org.sibadi.auditing.api.endpoints.PublicAPI._
import org.sibadi.auditing.api.model.{toApiError, ApiError, LoginResponse, PasswordResponse}
import org.sibadi.auditing.service._
import sttp.tapir.{Defaults, TapirFile}
import sttp.tapir.model.ServerRequest

class PublicRouter[F[_]: Monad](
  authenticator: Authenticator[F],
  estimateService: EstimateService[F],
  groupService: GroupService[F],
  kpiService: KpiService[F],
  reviewerService: ReviewerService[F],
  teacherService: TeacherService[F],
  topicService: TopicService[F]
) {

  def routes = List(createLogin, changePassword, publicUploadFileId)

  //TODO: How to set up login?
  private def createLogin =
    postLogin
      .serverLogic { userType =>
        ApiError.InternalError("Not implemented").cast.asLeft[LoginResponse].pure[F]
      }

  private def changePassword =
    editPassword
      .serverSecurityLogic { token =>
        authenticator.atLeastTeacher(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[PasswordResponse].pure[F]
      }

  private def publicUploadFileId =
    postApiPublicTopicsTopicIdKpiKpiIdFilesFileId
      .serverSecurityLogic { token =>
        authenticator.atLeastTeacher(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType => body =>
        estimateService
          .createEstimateFiles(body._1, body._2, body._3, defaultCreateFile)
          .leftMap(toApiError)
          .map(x => TapirFile)
          .value
//        ApiError.InternalError("Not implemented").cast.asLeft[TapirFile].pure[F]
      }
  def defaultCreateFile[F[_]](implicit sync: Sync[F]): ServerRequest => F[TapirFile] = _ => sync.blocking(Defaults.createTempFile())

  // https://github.com/softwaremill/tapir/blob/master/server/http4s-server/src/main/scala/sttp/tapir/server/http4s/Http4sServerOptions.scala
}
