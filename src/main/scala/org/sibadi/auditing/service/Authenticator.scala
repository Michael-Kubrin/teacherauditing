package org.sibadi.auditing.service

import cats.data.OptionT
import cats.effect.Resource
import cats.syntax.eq._
import cats.{Eq, Monad}
import org.sibadi.auditing.configs.AdminConfig
import org.sibadi.auditing.db.{ReviewerCredentialsDAO, TeacherCredentialsDAO}
import org.sibadi.auditing.service.Authenticator.UserType
import org.sibadi.auditing.service.Authenticator.UserType.Admin

class Authenticator[F[_]: Monad](
  teacherCredentialsDAO: TeacherCredentialsDAO[F],
  reviewerCredentialsDAO: ReviewerCredentialsDAO[F],
  adminConfig: AdminConfig
) {

  def isAdmin(token: String): OptionT[F, Admin] =
    authenticate(token).filter(_ === Admin()).map(_ => Admin())

  def authenticate(token: String): OptionT[F, UserType] =
    OptionT(teacherCredentialsDAO.getCredentialsByBearer(token))
      .map(_ => UserType.Teacher().cast)
      .orElse(OptionT(reviewerCredentialsDAO.getCredentialsByBearer(token)).map(_ => UserType.Reviewer().cast))
      .orElse(OptionT.pure((new Admin).cast).filter(_ => token === adminConfig.bearer))

}

object Authenticator {
  def apply[F[_]: Monad](
    teacherCredentialsDAO: TeacherCredentialsDAO[F],
    reviewerCredentialsDAO: ReviewerCredentialsDAO[F],
    adminConfig: AdminConfig
  ): Resource[F, Authenticator[F]] =
    Resource.pure {
      new Authenticator(teacherCredentialsDAO, reviewerCredentialsDAO, adminConfig)
    }

  sealed trait UserType {
    def cast: UserType = this
  }

  object UserType {
    final case class Teacher()  extends UserType
    final case class Reviewer() extends UserType
    final case class Admin()    extends UserType

    implicit val eq: Eq[UserType] = Eq.instance { case (one, other) =>
      one match {
        case Teacher() =>
          other match {
            case Teacher() => true
            case _         => false
          }
        case Reviewer() =>
          other match {
            case Reviewer() => true
            case _          => false
          }
        case Admin() =>
          other match {
            case Admin() => true
            case _       => false
          }
      }
    }
  }
}
