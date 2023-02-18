package org.sibadi.auditing.api.routes

import cats.effect.kernel.Sync
import cats.syntax.all._
import org.sibadi.auditing.api.endpoints.ReviewerActionsAPI._
import org.sibadi.auditing.api.model._
import org.sibadi.auditing.domain.EstimateStatus
import org.sibadi.auditing.service._
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

class ReviewerActionsRouter[F[_]: Sync](
  authenticator: Authenticator[F],
  estimateService: EstimateService[F],
  groupService: GroupService[F],
  kpiService: KpiService[F],
  reviewerService: ReviewerService[F],
  teacherService: TeacherService[F],
  topicService: TopicService[F]
) {

  implicit private def logger: Logger[F] = Slf4jLogger.getLogger

  def routes = List(adminEditStatus, getTeachers)

  private def adminEditStatus =
    putApiAdminTopicsTopicIdKpiKpiIdTeachersTeacherIdStatus
      .serverSecurityLogic { token =>
        authenticator
          .atLeastReviewer(token)
          .toRight(ApiError.Unauthorized("Unauthorized").cast)
          .value
          .handleErrorWith(throwableToUnexpected[F, Authenticator.UserType])
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
          .leftSemiflatMap(toApiError[F])
          .value
          .handleErrorWith(throwableToUnexpected[F, Unit])
      }

  private def getTeachers =
    getApiReviewerTeachers
      .serverSecurityLogic { token =>
        authenticator
          .atLeastReviewer(token)
          .toRight(ApiError.Unauthorized("Unauthorized").cast)
          .value
          .handleErrorWith(throwableToUnexpected[F, Authenticator.UserType])
      }
      .serverLogic { _ => teacherId =>
        teacherService
          .getTeacher(teacherId)
          .map(teacher =>
            TeacherResponse(
              teacher.id,
              teacher.firstName,
              teacher.lastName,
              teacher.middleName,
              teacher.groups.map(group => TeacherGroupItemResponse(group.id, group.name))
            )
          )
          .leftSemiflatMap(toApiError[F])
          .value
          .handleErrorWith(throwableToUnexpected[F, TeacherResponse])
      }

//Todo: check teacherId

//  private def adminGetEstimate =
//    getApiReviewerEstimate
//      .serverSecurityLogic { token =>
//        authenticator
//          .atLeastReviewer(token)
//          .toRight(ApiError.Unauthorized("Unauthorized").cast)
//          .value
//          .handleErrorWith(throwableToUnexpected[F, Authenticator.UserType])
//      }
//      .serverLogic { userType => body =>
//        reviewerService
//          .getEstimate(kpiId = body, teacherId = body)
//          .leftSemiflatMap(toApiError[F])
//          .value
//          .handleErrorWith(throwableToUnexpected[F, EstimateResponse])
//      }

}
