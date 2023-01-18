package org.sibadi.auditing.api.routes

import cats.Monad
import cats.effect.Sync
import cats.syntax.all._
import org.sibadi.auditing.api.endpoints.KpiAPI._
import org.sibadi.auditing.api.model._
import org.sibadi.auditing.service._
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

class KpiRouter[F[_]: Sync](
  authenticator: Authenticator[F],
  estimateService: EstimateService[F],
  groupService: GroupService[F],
  kpiService: KpiService[F],
  reviewerService: ReviewerService[F],
  teacherService: TeacherService[F],
  topicService: TopicService[F]
) {

  private implicit def logger: Logger[F] = Slf4jLogger.getLogger

  def routes = List(
    adminCreateTopicKpi,
    adminGetTopicKpi,
    adminEditTopicKpiId,
    adminDeleteTopicKpiId
  )

  //Not sure about .map{id=>}
  private def adminCreateTopicKpi =
    postApiAdminTopicsTopicIdKpi
      .serverSecurityLogic { token =>
        authenticator.isAdmin(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value.handleErrorWith(throwableToUnexpected[F, Authenticator.UserType])
      }
      .serverLogic { userType => body =>
        kpiService
          .createKpi(body._2.title)
          .map { id =>
            ResponseId(id.toString)
          }
          .leftSemiflatMap(toApiError[F])
          .value.handleErrorWith(throwableToUnexpected[F, ResponseId])
      }

  //TODO: Not sure about it
  private def adminGetTopicKpi =
    getApiAdminTopicsTopicIdKpi
      .serverSecurityLogic { token =>
        authenticator.atLeastReviewer(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value.handleErrorWith(throwableToUnexpected[F, Authenticator.UserType])
      }
      .serverLogic { userType => body =>
        kpiService.getAllKpi
          .leftSemiflatMap(toApiError[F])
          .map(_.map(x => TopicKpiResponse(x.id, x.title)))
          .value.handleErrorWith(throwableToUnexpected[F, List[org.sibadi.auditing.api.model.TopicKpiResponse]])
      }

  private def adminEditTopicKpiId =
    putApiAdminTopicsTopicIdKpiKpiId
      .serverSecurityLogic { token =>
        authenticator.isAdmin(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value.handleErrorWith(throwableToUnexpected[F, Authenticator.UserType])
      }
      .serverLogic { userType => body =>
        kpiService
          .updateKpi(body._1, body._2)
          .leftSemiflatMap(toApiError[F])
          .value.handleErrorWith(throwableToUnexpected[F, Unit])
      }

  private def adminDeleteTopicKpiId =
    deleteApiAdminTopicsTopicIdKpiKpiId
      .serverSecurityLogic { token =>
        authenticator.isAdmin(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value.handleErrorWith(throwableToUnexpected[F, Authenticator.UserType])
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[Unit].pure[F]
      }

}
