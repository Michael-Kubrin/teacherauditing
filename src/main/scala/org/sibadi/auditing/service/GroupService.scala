package org.sibadi.auditing.service

import cats.data.EitherT
import cats.effect.{MonadCancel, Resource}
import cats.syntax.all._
import org.sibadi.auditing.db
import org.sibadi.auditing.db.{Group, GroupDAO}
import org.sibadi.auditing.domain.errors.AppError

import java.util.UUID

class GroupService[F[_]](groupDao: GroupDAO[F])(implicit M: MonadCancel[F, Throwable]) {

  def createGroup(title: String): EitherT[F, AppError, Unit] =
    for {
      groupId <- EitherT.pure(UUID.randomUUID().toString)
      _ <- EitherT(
        groupDao
          .insert(db.Group(groupId, title, None))
          .map(_.asRight[AppError])
          .handleError(throwable => AppError.Unexpected(throwable).asLeft[Unit])
      )
    } yield ()

  def getGroup(groupId: String): EitherT[F, AppError, Option[Group]] =
    EitherT(
      groupDao
        .get(groupId)
        .map(_.toRight(AppError.GroupDoesNotExists(groupId).cast))
        .handleError(throwable => AppError.Unexpected(throwable).asLeft[Group])
    )

  def getAllGroups: EitherT[F, AppError, List[Group]] =
    EitherT(
      groupDao.getAll
        .map(_.asRight[AppError])
        .handleError(throwable => AppError.Unexpected(throwable).asLeft[List[Group]])
    )

  def updateGroup(groupId: String, groupName: String): EitherT[F, AppError, Unit] =
    EitherT(
      groupDao
        .update(db.Group(groupId, groupName, None))
        .map(_.asRight[AppError])
        .handleError(throwable => AppError.Unexpected(throwable).asLeft[Unit])
    )
}

object GroupService {}
