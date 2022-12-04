package org.sibadi.auditing.service

import cats.Applicative
import cats.data.OptionT
import cats.effect.Resource
import org.sibadi.auditing.service.Authenticator.UserType.Admin
import org.sibadi.auditing.service.Authenticator.UserType

class Authenticator[F[_]: Applicative] {

  def authenticate(token: String): OptionT[F, UserType] = OptionT.pure(new Admin)

  def isAdmin(token: String): OptionT[F, Admin] = OptionT.pure(new Admin)

}

object Authenticator {
  sealed trait UserType
  object UserType {
    class Teacher  extends UserType
    class Reviewer extends UserType
    class Admin    extends UserType
  }

  def apply[F[_]: Applicative](): Resource[F, Authenticator[F]] =
    Resource.pure {
      new Authenticator()
    }
}
