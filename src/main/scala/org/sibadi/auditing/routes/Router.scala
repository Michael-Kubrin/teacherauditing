package org.sibadi.auditing.routes

import cats.Monad
import cats.effect.Async
import cats.syntax.all._
import org.http4s.HttpRoutes
import org.sibadi.auditing.domain.ApiError
import sttp.tapir.server.http4s.Http4sServerOptions
import org.sibadi.auditing.endpoints.GeneratedEndpoints._
import org.sibadi.auditing.service.Authenticator


class Router[F[_]: Monad](authenticator: Authenticator[F]) extends RouterOps[F] {

  // TODO handle all endpoints with 500 return

  def routes = List(adminCreateTopic)

  def adminCreateTopic =
    postApiAdminTopics
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType =>
        body =>
          ApiError.InternalError("Not implemented").cast.asLeft[ResponseId].pure[F]
      }

}
