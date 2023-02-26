package org.sibadi.auditing.api.routes

import cats.effect.Sync
import cats.syntax.all._
import org.sibadi.auditing.api.endpoints.ReviewersAPI._
import org.sibadi.auditing.api.model.{ApiError, CredentialsResponseDto, ReviewerItemResponseDto, toApiError}
import org.sibadi.auditing.service._
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

class ReviewersRouter[F[_]: Sync](
  authenticator: Authenticator[F],
  estimateService: EstimateService[F],
  groupService: GroupService[F],
  kpiService: KpiService[F],
  reviewerService: ReviewerService[F],
  teacherService: TeacherService[F],
  topicService: TopicService[F]
) {

  private implicit def logger: Logger[F] = Slf4jLogger.getLogger

  def routes = List(adminCreateReviewers, adminGetReviewers, adminEditReviewers)

  private def adminCreateReviewers =
    postApiAdminReviewers
      .serverSecurityLogic { token =>
        authenticator.isAdmin(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value.handleErrorWith(throwableToUnexpected[F, Authenticator.UserType])
      }
      .serverLogic { userType => body =>
        reviewerService
          .createReviewer(
            firstName = body.name,
            lastName = body.surName,
            middleName = body.middleName,
            login = body.login
          )
          .leftSemiflatMap(toApiError[F])
          .map(reviewer => CredentialsResponseDto(id = reviewer.id, password = reviewer.password))
          .value.handleErrorWith(throwableToUnexpected[F, CredentialsResponseDto])
      }

  private def adminGetReviewers =
    getApiAdminReviewers
      .serverSecurityLogic { token =>
        authenticator.isAdmin(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value.handleErrorWith(throwableToUnexpected[F, Authenticator.UserType])
      }
      .serverLogic { userType => body =>
        reviewerService.getAllReviewers
          .map(_.map(reviewer => ReviewerItemResponseDto(reviewer.id, reviewer.firstName, reviewer.lastName, reviewer.middleName)))
          .leftSemiflatMap(toApiError[F])
          .value.handleErrorWith(throwableToUnexpected[F, List[org.sibadi.auditing.api.model.ReviewerItemResponseDto]])
      }

  private def adminEditReviewers =
    putApiAdminReviewersId
      .serverSecurityLogic { token =>
        authenticator.isAdmin(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value.handleErrorWith(throwableToUnexpected[F, Authenticator.UserType])
      }
      .serverLogic { userType => body =>
        reviewerService
          .updateReviewer(body._2.name, body._2.surName, body._2.middleName, body._1)
          .leftSemiflatMap(toApiError[F])
          .value.handleErrorWith(throwableToUnexpected[F, Unit])
      }

}
