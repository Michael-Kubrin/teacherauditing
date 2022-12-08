package org.sibadi.auditing.api.routes

import cats.Monad
import org.sibadi.auditing.api.endpoints.ReviewersAPI.{getApiAdminReviewers, postApiAdminReviewers, putApiAdminReviewersId}
import org.sibadi.auditing.api.model.{toApiError, ApiError}
import org.sibadi.auditing.service.{Authenticator, ReviewerService}

class ReviewersRouter[F[_]: Monad](
  authenticator: Authenticator[F],
  reviewerService: ReviewerService[F]
) {

  def routes = List(adminCreateReviewers, adminGetReviewers, adminEditReviewers)

  private def adminCreateReviewers =
    postApiAdminReviewers
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
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
          .value
      }

  private def adminGetReviewers =
    getApiAdminReviewers
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType => body =>
        reviewerService
          .getReviewer(reviewerId = ???)
          .map { response =>
            response.id
          }
          .leftMap(toApiError)
          .value
      }

  private def adminEditReviewers =
    putApiAdminReviewersId
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType => body =>
        for {
          takeData <- reviewerService.getAllReviewers
          firstName  = takeData.map(reviewer => reviewer.firstName)
          lastName   = takeData.map(reviewer => reviewer.lastName)
          middleName = takeData.map(reviewer => reviewer.middleName)
          teacherId  = takeData.map(reviewer => reviewer.id)
          reviewers <- reviewerService.updateReviewer(firstName, lastName, middleName, teacherId)
        } yield reviewers
      }

}
