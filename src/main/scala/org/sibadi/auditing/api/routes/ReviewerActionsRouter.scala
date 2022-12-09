package org.sibadi.auditing.api.routes

import cats.Monad
import org.sibadi.auditing.api.endpoints.ReviewerActionsAPI._
import org.sibadi.auditing.api.model._
import org.sibadi.auditing.domain.EstimateStatus
import org.sibadi.auditing.service._

class ReviewerActionsRouter[F[_]: Monad](
  authenticator: Authenticator[F],
  estimateService: EstimateService[F],
  groupService: GroupService[F],
  kpiService: KpiService[F],
  reviewerService: ReviewerService[F],
  teacherService: TeacherService[F],
  topicService: TopicService[F]
) {

  def routes = List(adminEditStatus)

  private def adminEditStatus =
    putApiAdminTopicsTopicIdKpiKpiIdTeachersTeacherIdStatus
      .serverSecurityLogic { token =>
        authenticator.atLeastReviewer(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType => body =>
        val domainStatus = body._4.newstatus match {
          case ReviewStatus.Waiting  => EstimateStatus.Waiting
          case ReviewStatus.Accepted => EstimateStatus.Accepted
          case ReviewStatus.Declined => EstimateStatus.Declined
          case ReviewStatus.Reviewed => EstimateStatus.Reviewed
        }
        estimateService
          .updateEstimate(topicId = body._1, kpiId = body._2, teacherId = body._3, newStatus = domainStatus)
          .leftMap(toApiError)
          .value
      }

}
