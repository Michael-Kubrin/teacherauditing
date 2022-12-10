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

  def atLeastTeacher(token: String): OptionT[F, UserType] =
    isTeacher(token).orElse(isReviewer(token)).orElse(isAdmin(token))

  def atLeastReviewer(token: String): OptionT[F, UserType] =
    isReviewer(token).orElse(isAdmin(token))

  def isTeacher(token: String): OptionT[F, UserType] =
    OptionT(teacherCredentialsDAO.getCredentialsByBearer(token)).map(u => UserType.Teacher(u.id).cast)

  def isReviewer(token: String): OptionT[F, UserType] =
    OptionT(reviewerCredentialsDAO.getCredentialsByBearer(token)).map(u => UserType.Reviewer(u.id).cast)

  def isAdmin(token: String): OptionT[F, UserType] =
    OptionT.pure(Admin("").cast).filter(_ => token === adminConfig.bearer)

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
    val id: String
    def cast: UserType = this
  }

  object UserType {
    final case class Teacher(override val id: String)  extends UserType
    final case class Reviewer(override val id: String) extends UserType
    final case class Admin(override val id: String)    extends UserType

    implicit val eq: Eq[UserType] = Eq.instance { case (one, other) =>
      one match {
        case Teacher(_) =>
          other match {
            case Teacher(_) => true
            case _          => false
          }
        case Reviewer(_) =>
          other match {
            case Reviewer(_) => true
            case _           => false
          }
        case Admin(_) =>
          other match {
            case Admin(_) => true
            case _        => false
          }
      }
    }
  }
}
