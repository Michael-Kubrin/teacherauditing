package org.sibadi.auditing.service

import cats.data.{EitherT, OptionT}
import cats.effect.{MonadCancel, Resource}
import cats.syntax.all._
import org.sibadi.auditing.db
import org.sibadi.auditing.db.{GroupDAO, KpiDAO, KpiGroupDAO}
import org.sibadi.auditing.domain.{FullGroup, FullKpi}
import org.sibadi.auditing.domain.errors.AppError

import java.util.UUID

class GroupService[F[_]](
  groupDao: GroupDAO[F],
  kpiDAO: KpiDAO[F],
  kpiGroupDao: KpiGroupDAO[F]
)(implicit M: MonadCancel[F, Throwable]) {

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

  def getAllGroups: EitherT[F, AppError, List[FullGroup]] = {

    val dbLogic: F[List[FullGroup]] = for {
      groups <- groupDao.getAll
      fullGroups <- groups.map { group =>
        for {
          links <- kpiGroupDao.getByGroupId(group.id)
          kpis <- links
            .map { link =>
              kpiDAO.get(link.kpiId)
            }
            .flatTraverse(_.map(_.toList))
        } yield FullGroup(group.id, group.title, kpis.map(kpi => FullKpi(kpi.id, kpi.title)))
      }.sequence
    } yield fullGroups

    EitherT(
      dbLogic
        .map(_.asRight[AppError])
        .handleError(throwable => AppError.Unexpected(throwable).asLeft[List[FullGroup]])
    )
  }

  def addKpiToGroup(groupId: String, kpiId: String): EitherT[F, AppError, Unit] = {

    def linkF = EitherT(
      kpiGroupDao.insert(db.KpiGroup(kpiId, groupId)).map(_.asRight[AppError]).handleError(throwable => AppError.Unexpected(throwable).asLeft[Unit])
    )

    for {
      _ <- getGroupF(groupId) // check group exists
      _ <- getKpiF(kpiId) // check kpi exists
      _ <- linkF
    } yield ()
  }

  def removeKpiFromGroup(groupId: String, kpiId: String): EitherT[F, AppError, Unit] = {

    def removeLinkF = EitherT(
      kpiGroupDao.delete(db.KpiGroup(kpiId, groupId)).map(_.asRight[AppError]).handleError(throwable => AppError.Unexpected(throwable).asLeft[Unit])
    )

    for {
      _ <- getGroupF(groupId) // check group exists
      _ <- getKpiF(kpiId) // check kpi exists
      _ <- removeLinkF
    } yield ()
  }

  private def getGroupF(groupId: String): EitherT[F, AppError, db.Group] =
    OptionT(groupDao.get(groupId).handleError(_ => None)).toRight(AppError.GroupDoesNotExists(groupId).cast)

  private def getKpiF(kpiId: String): EitherT[F, AppError, db.Kpi] =
    OptionT(kpiDAO.get(kpiId).handleError(_ => None)).toRight(AppError.KpiDoesNotExists(kpiId).cast)

}

object GroupService {

  def apply[F[_]](groupDao: GroupDAO[F], kpiDAO: KpiDAO[F], kpiGroupDao: KpiGroupDAO[F])(implicit
    M: MonadCancel[F, Throwable]
  ): Resource[F, GroupService[F]] =
    Resource.pure(new GroupService(groupDao, kpiDAO, kpiGroupDao))

}
