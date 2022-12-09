package org.sibadi.auditing.api.routes

import cats.Monad
import cats.syntax.applicative._
import cats.syntax.either._
import org.sibadi.auditing.api.endpoints.ReviewersAPI._
import org.sibadi.auditing.api.model.{toApiError, ApiError, ResponseIdPassword, ReviewerResponse}
import org.sibadi.auditing.service._

class ReviewersRouter[F[_]: Monad](
  authenticator: Authenticator[F],
  estimateService: EstimateService[F],
  groupService: GroupService[F],
  kpiService: KpiService[F],
  reviewerService: ReviewerService[F],
  teacherService: TeacherService[F],
  topicService: TopicService[F]
) {

  def routes = List(adminCreateReviewers, adminGetReviewers, adminEditReviewers)

  private def adminCreateReviewers =
    postApiAdminReviewers
      .serverSecurityLogic { token =>
        authenticator.isAdmin(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType => body =>
        reviewerService
          .createReviewer(
            firstName = body.name,
            lastName = body.surName,
            middleName = body.middleName,
            login = body.login
          )
          .leftMap(toApiError)
          .map(reviewer => ResponseIdPassword(id = reviewer.id, password = reviewer.password))
          .value
      }

  private def adminGetReviewers =
    getApiAdminReviewers
      .serverSecurityLogic { token =>
        authenticator.isAdmin(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType => body =>
        reviewerService.getAllReviewers
          .map(_.map(reviewer => ReviewerResponse(reviewer.id, reviewer.firstName, reviewer.lastName, reviewer.middleName)))
          .leftMap(toApiError)
          .value
      }

  private def adminEditReviewers =
    putApiAdminReviewersId
      .serverSecurityLogic { token =>
        authenticator.isAdmin(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[Unit].pure[F]
      }

}
