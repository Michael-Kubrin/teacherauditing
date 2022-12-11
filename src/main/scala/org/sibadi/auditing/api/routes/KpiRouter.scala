package org.sibadi.auditing.api.routes

import cats.Monad
import cats.syntax.applicative._
import cats.syntax.either._
import org.sibadi.auditing.api.endpoints.KpiAPI._
import org.sibadi.auditing.api.model._
import org.sibadi.auditing.service._

class KpiRouter[F[_]: Monad](
  authenticator: Authenticator[F],
  estimateService: EstimateService[F],
  groupService: GroupService[F],
  kpiService: KpiService[F],
  reviewerService: ReviewerService[F],
  teacherService: TeacherService[F],
  topicService: TopicService[F]
) {

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
        authenticator.isAdmin(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType => body =>
        kpiService
          .createKpi(body._2.title)
          .map { id =>
            ResponseId(id.toString)
          }
          .leftMap(toApiError)
          .value
      }

  //TODO: Not sure about it
  private def adminGetTopicKpi =
    getApiAdminTopicsTopicIdKpi
      .serverSecurityLogic { token =>
        authenticator.atLeastReviewer(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType => body =>
        kpiService.getAllKpi
          .leftMap(toApiError)
          .map(_.map(x => TopicKpiResponse(x.id, x.title)).toList)
          .value
      }

  private def adminEditTopicKpiId =
    putApiAdminTopicsTopicIdKpiKpiId
      .serverSecurityLogic { token =>
        authenticator.isAdmin(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType => body =>
        kpiService
          .updateKpi(body._1, body._2)
          .leftMap(toApiError)
          .value
      }

  private def adminDeleteTopicKpiId =
    deleteApiAdminTopicsTopicIdKpiKpiId
      .serverSecurityLogic { token =>
        authenticator.isAdmin(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[Unit].pure[F]
      }

}
