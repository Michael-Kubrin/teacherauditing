package org.sibadi.auditing.api.routes

import cats.syntax.all._

import cats.Monad
import org.sibadi.auditing.api.endpoints.PublicAPI._
import org.sibadi.auditing.api.model.{ApiError, LoginResponse, PasswordResponse}
import org.sibadi.auditing.service.Authenticator

class PublicRouter[F[_]: Monad](
  authenticator: Authenticator[F]
) {

  def routes = List(createLogin, changePassword)

  private def createLogin =
    postLogin
      .serverLogic { userType =>
        ApiError.InternalError("Not implemented").cast.asLeft[LoginResponse].pure[F]
      }

  private def changePassword =
    editPassword
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[PasswordResponse].pure[F]
      }
}
