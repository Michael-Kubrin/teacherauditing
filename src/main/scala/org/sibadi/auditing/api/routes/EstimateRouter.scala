package org.sibadi.auditing.api.routes

import cats.Monad
import cats.syntax.either._
import cats.syntax.applicative._
import org.sibadi.auditing.api.endpoints.ReviewerActionsAPI._
import org.sibadi.auditing.api.endpoints.PublicAPI._
import org.sibadi.auditing.api.endpoints.TeacherActionsAPI._
import org.sibadi.auditing.api.model.{ApiError, toApiError}
import org.sibadi.auditing.service.{Authenticator, EstimateService}
import sttp.tapir.TapirFile

class EstimateRouter[F[_]: Monad](
  authenticator: Authenticator[F],
  estimateService: EstimateService[F]
) {

  def routes = List(adminEditStatus, publicEstimate, publicUploadFile, publicUploadFileId)

  private def adminEditStatus =
    putApiAdminTopicsTopicIdKpiKpiIdTeachersTeacherIdStatus
      .serverSecurityLogic { token =>
        authenticator.atLeastReviewer(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType => body =>
        estimateService
          .updateEstimate(topicId = body._1, kpiId = body._2, teacherId = body._3)
          .leftMap(toApiError)
          .value
      }

  private def publicEstimate =
    postApiPublicTopicsTopicIdKpiKpiIdEstimate
      .serverSecurityLogic { token =>
        authenticator.isTeacher(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType => body =>
        estimateService
          .createEstimate(topicId = body._1, kpiId = body._2, teacherId = userType.id, score = body._3.score)
          .leftMap(toApiError)
          .value
      }

  def publicUploadFile =
    postApiPublicTopicsTopicIdKpiKpiIdFiles
      .serverSecurityLogic { token =>
        authenticator.isTeacher(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType => body =>
        estimateService
          .createEstimateFiles(topicId = body._1, kpiId = body._2, userType.id, body._3)
          .leftMap(toApiError)
          .value
      }

    def publicUploadFileId =
      postApiPublicTopicsTopicIdKpiKpiIdFilesFileId
        .serverSecurityLogic { token =>
          authenticator.atLeastTeacher(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
        }
        .serverLogic { userType =>
          body =>
            ApiError.InternalError("Not implemented").cast.asLeft[TapirFile].pure[F]
        }

}
