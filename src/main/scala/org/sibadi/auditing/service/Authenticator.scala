package org.sibadi.auditing.service

import cats.data.OptionT
import org.sibadi.auditing.service.Authenticator.UserType

class Authenticator[F[_]] {

  def authenticate(token: String): OptionT[F, UserType] = ???

}

object Authenticator {
  sealed trait UserType
  object UserType {
    object Teacher extends UserType
    object Reviewer extends UserType
    object Admin extends UserType
  }
}
