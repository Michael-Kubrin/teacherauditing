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
//        ApiError.InternalError("Not implemented").cast.asLeft[ResponseId].pure[F]
      }

  //TODO: Not sure about it
  private def adminGetTopicKpi =
    getApiAdminTopicsTopicIdKpi
      .serverSecurityLogic { token =>
        authenticator.atLeastReviewer(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType => kpiId =>
        kpiService
          .getByKpiId(kpiId)
          .leftMap(toApiError)
          .map(_.map(x => TopicKpiResponse(x.kpiId, x.groupId)).toList)
          .value
//        ApiError.InternalError("Not implemented").cast.asLeft[List[TopicKpiResponse]].pure[F]
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
