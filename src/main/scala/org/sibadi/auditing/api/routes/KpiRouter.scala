package org.sibadi.auditing.api.routes

import cats.Monad
import org.sibadi.auditing.api.endpoints.KpiAPI._
import org.sibadi.auditing.api.model.ApiError
import org.sibadi.auditing.api.model._
import org.sibadi.auditing.service.{Authenticator, KpiService}

class KpiRouter[F[_]: Monad](
  authenticator: Authenticator[F],
  kpiService: KpiService[F]
                            ) {

  private def adminCreateTopicKpi =
    postApiAdminTopicsTopicIdKpi
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType =>
        body =>

      }

  private def adminGetTopicKpi =
    getApiAdminTopicsTopicIdKpi
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType =>
        body =>

      }

  private def adminEditTopicKpiId =
    putApiAdminTopicsTopicIdKpiKpiId
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType =>
        body =>

      }

  private def adminDeleteTopicKpiId =
    deleteApiAdminTopicsTopicIdKpiKpiId
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType =>
        body =>

      }

}
