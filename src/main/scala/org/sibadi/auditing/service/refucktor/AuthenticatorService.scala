package org.sibadi.auditing.service.refucktor

import cats.data.OptionT
import cats.effect.Resource
import cats.syntax.eq._
import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.syntax.option._
import cats.{Eq, Monad}
import org.sibadi.auditing.configs.AdminConfig
import org.sibadi.auditing.db.DbComponent
import org.sibadi.auditing.service.refucktor.AuthenticatorService.UserType
import org.sibadi.auditing.service.refucktor.AuthenticatorService.UserType.Admin
import org.sibadi.auditing.util.{HashGenerator, TokenGenerator}
import org.typelevel.log4cats.Logger

class AuthenticatorService[F[_]: Monad](
  db: DbComponent[F],
  adminConfig: AdminConfig,
  tokenGenerator: TokenGenerator[F],
  hashGen: HashGenerator[F]
) {

  def atLeastTeacher(token: String): OptionT[F, UserType] =
    isTeacher(token).orElse(isReviewer(token)).orElse(isAdmin(token))

  def atLeastReviewer(token: String): OptionT[F, UserType] =
    isReviewer(token).orElse(isAdmin(token))

  def isTeacher(token: String): OptionT[F, UserType] =
    OptionT(db.teacherCredentialsDAO.getCredentialsByBearer(token)).map(u => UserType.Teacher(u.id).cast)

  private def isReviewer(token: String): OptionT[F, UserType] =
    OptionT(db.reviewerCredentialsDAO.getCredentialsByBearer(token)).map(u => UserType.Reviewer(u.id).cast)

  def isAdmin(token: String): OptionT[F, UserType] =
    OptionT.pure(Admin("").cast).filter(_ => token === adminConfig.bearer)

  def authenticate(login: String, password: String)(implicit L: Logger[F]): OptionT[F, String] =
    authAsTeacher(login, password).orElse(authAsReviewer(login, password))

  private def authAsTeacher(login: String, password: String)(implicit L: Logger[F]): OptionT[F, String] =
    OptionT(db.teacherCredentialsDAO.getByLogin(login))
      .semiflatTap { creds =>
        L.info(s"Found creds by login $login. Bearer: ${creds.bearer}")
      }
      .flatTapNone(L.error(s"Not found creds by login $login"))
      .flatMapF { creds =>
        hashGen.checkPassword(password, creds.passwordHash).map { isEqual =>
          if (isEqual) creds.some else none
        }
      }
      .semiflatMap { creds =>
        for {
          newBearer <- tokenGenerator.generate // new bearer needed for invalidate other sessions
          _         <- db.teacherCredentialsDAO.deleteCredentials(creds.id, creds.login)
          _         <- db.teacherCredentialsDAO.insertCredentials(creds.copy(bearer = newBearer))
        } yield newBearer
      }

  private def authAsReviewer(login: String, password: String)(implicit L: Logger[F]): OptionT[F, String] =
    OptionT(db.reviewerCredentialsDAO.getByLogin(login))
      .semiflatTap { creds =>
        L.info(s"Found creds by login $login. Bearer: ${creds.bearer}")
      }
      .flatTapNone(L.error(s"Not found creds by login $login"))
      .flatMapF { creds =>
        hashGen.checkPassword(password, creds.passwordHash).map { isEqual =>
          if (isEqual) creds.some else none
        }
      }
      .semiflatMap { creds =>
        for {
          newBearer <- tokenGenerator.generate // new bearer needed for invalidate other sessions
          _         <- db.reviewerCredentialsDAO.deleteCredentials(creds.id, creds.login)
          _         <- db.reviewerCredentialsDAO.insertCredentials(creds.copy(bearer = newBearer))
        } yield newBearer
      }

}

object AuthenticatorService {
  def apply[F[_]: Monad](
    dbComponent: DbComponent[F],
    adminConfig: AdminConfig,
    tokenGenerator: TokenGenerator[F],
    hashGen: HashGenerator[F]
  ): Resource[F, AuthenticatorService[F]] =
    Resource.pure {
      new AuthenticatorService(dbComponent, adminConfig, tokenGenerator, hashGen)
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
