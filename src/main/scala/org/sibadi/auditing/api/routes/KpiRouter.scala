package org.sibadi.auditing.api.routes

import cats.syntax.all._

import cats.Monad
import org.sibadi.auditing.api.endpoints.KpiAPI._
import org.sibadi.auditing.api.endpoints.KpiGroupAPI._
import org.sibadi.auditing.api.endpoints.KpiTeacherAPI._
import org.sibadi.auditing.api.endpoints.TeacherActionsAPI._
import org.sibadi.auditing.api.model.ApiError
import org.sibadi.auditing.api.model._
import org.sibadi.auditing.service.{Authenticator, KpiService}

class KpiRouter[F[_]: Monad](
  authenticator: Authenticator[F],
  kpiService: KpiService[F]
) {

  def routes = List(
    adminCreateTopicKpi,
    adminGetTopicKpi,
    adminEditTopicKpiId,
    adminDeleteTopicKpiId,
    adminCreateTeacherId,
    adminDeleteTeacherId,
    adminEditGroups,
    adminDeleteGropus,
    publicGetKpi
  )

  private def adminCreateTopicKpi =
    postApiAdminTopicsTopicIdKpi
      .serverSecurityLogic { token =>
        authenticator.isAdmin(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[ResponseId].pure[F]
      }

  private def adminGetTopicKpi =
    getApiAdminTopicsTopicIdKpi
      .serverSecurityLogic { token =>
        authenticator.atLeastReviewer(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType =>
        body =>
          ApiError.InternalError("Not implemented").cast.asLeft[List[TopicKpiResponse]].pure[F]
      }

  private def adminEditTopicKpiId =
    putApiAdminTopicsTopicIdKpiKpiId
      .serverSecurityLogic { token =>
        authenticator.isAdmin(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType =>
        body =>
          ApiError.InternalError("Not implemented").cast.asLeft[Unit].pure[F]
      }

  private def adminDeleteTopicKpiId =
    deleteApiAdminTopicsTopicIdKpiKpiId
      .serverSecurityLogic { token =>
        authenticator.isAdmin(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType =>
        body =>
          ApiError.InternalError("Not implemented").cast.asLeft[Unit].pure[F]
      }

  private def adminCreateTeacherId =
    postApiAdminTopicsTopicIdKpiKpiIdTeacherTeacherId
      .serverSecurityLogic { token =>
        authenticator.isAdmin(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType =>
        body =>
          ApiError.InternalError("Not implemented").cast.asLeft[Unit].pure[F]
      }

  private def adminDeleteTeacherId =
    deleteApiAdminTopicsTopicIdKpiKpiIdTeacherTeacherId
      .serverSecurityLogic { token =>
        authenticator.isAdmin(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[String].pure[F]
      }

  private def adminEditGroups =
    putApiAdminGroupsGroupIdTopicsTopicIdKpiKpiId
      .serverSecurityLogic { token =>
        authenticator.isAdmin(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType =>
        body =>
          ApiError.InternalError("Not implemented").cast.asLeft[String].pure[F]
      }

  private def adminDeleteGropus =
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

  private def publicGetKpi =
    getApiPublicTopicsTopicIdKpi
      .serverSecurityLogic { token =>
        authenticator.isTeacher(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType =>
        body =>
          ApiError.InternalError("Not implemented").cast.asLeft[String].pure[F]
      }
}
