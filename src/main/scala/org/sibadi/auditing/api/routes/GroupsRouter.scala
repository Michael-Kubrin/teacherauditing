package org.sibadi.auditing.api.routes

import cats.Monad
import org.sibadi.auditing.api.endpoints.GroupsAPI._
import org.sibadi.auditing.api.model._
import org.sibadi.auditing.service.{Authenticator, GroupService}
class GroupsRouter[F[_]: Monad](
  authenticator: Authenticator[F],
  groupService: GroupService[F]
) {

  def routes = List(adminCreateGroups, adminGetGroups)

  private def adminCreateGroups =
    postApiAdminGroups
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType => body =>
        groupService
          .createGroup(title = body.name)
          .leftMap(toApiError)
          .value
      }

  private def adminGetGroups =
    getApiAdminGroups
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType => body =>
        groupService
          .getGroup(groupId = ???)
          .leftMap(toApiError)
          .value
      }

}
