package org.sibadi.auditing.api.routes

import cats.Monad
import cats.syntax.all._
import cats.effect.Sync
import org.sibadi.auditing.api.endpoints.GroupsAPI._
import org.sibadi.auditing.api.model._
import org.sibadi.auditing.service._
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

class GroupsRouter[F[_]: Sync](
  authenticator: Authenticator[F],
  estimateService: EstimateService[F],
  groupService: GroupService[F],
  kpiService: KpiService[F],
  reviewerService: ReviewerService[F],
  teacherService: TeacherService[F],
  topicService: TopicService[F]
) {

  private implicit def logger: Logger[F] = Slf4jLogger.getLogger

  def routes = List(adminCreateGroups, adminGetGroups)

  private def adminCreateGroups =
    postApiAdminGroups
      .serverSecurityLogic { token =>
        authenticator.isAdmin(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value.handleErrorWith(throwableToUnexpected[F, Authenticator.UserType])
      }
      .serverLogic { userType => body =>
        groupService
          .createGroup(title = body.name)
          .leftSemiflatMap(toApiError[F])
          .value.handleErrorWith(throwableToUnexpected[F, Unit])
      }

  private def adminGetGroups =
    getApiAdminGroups
      .serverSecurityLogic { token =>
        authenticator.atLeastReviewer(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value.handleErrorWith(throwableToUnexpected[F, Authenticator.UserType])
      }
      .serverLogic { userType => body =>
        groupService.getAllGroups
          .leftSemiflatMap(toApiError[F])
          .map(_.map(group => GroupResponseItemDto(group.id, group.title)))
          .value.handleErrorWith(throwableToUnexpected[F, List[GroupResponseItemDto]])
      }

}
