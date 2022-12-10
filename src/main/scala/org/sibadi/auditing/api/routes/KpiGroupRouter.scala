package org.sibadi.auditing.api.routes

import cats.Monad
import cats.syntax.applicative._
import cats.syntax.either._
import org.sibadi.auditing.api.endpoints.KpiGroupAPI._
import org.sibadi.auditing.api.model._
import org.sibadi.auditing.service._

class KpiGroupRouter[F[_]: Monad](
  authenticator: Authenticator[F],
  estimateService: EstimateService[F],
  groupService: GroupService[F],
  kpiService: KpiService[F],
  reviewerService: ReviewerService[F],
  teacherService: TeacherService[F],
  topicService: TopicService[F]
) {

  def routes = List(adminEditGroups, adminDeleteGroups)

  private def adminEditGroups =
    putApiAdminGroupsGroupIdTopicsTopicIdKpiKpiId
      .serverSecurityLogic { token =>
        authenticator.isAdmin(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType => body =>
//        groupService
//          .updateGroup(body._2.groupIds, body._1)
//          .leftMap(toApiError)
//          .value
        ApiError.InternalError("Not implemented").cast.asLeft[String].pure[F]
      }

  private def adminDeleteGroups =
    deleteApiAdminGroupsGroupIdTopicsTopicIdKpiKpiId
      .serverSecurityLogic { token =>
        authenticator
          .isAdmin(token)
          .toRight(ApiError.Unauthorized("Unauthorized").cast)
          .value
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[String].pure[F]
      }

}
