package org.sibadi.auditing.service

import cats.data.{EitherT, OptionT}
import cats.effect.{MonadCancel, Resource}
import cats.syntax.applicativeError._
import cats.syntax.either._
import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.syntax.traverse._
import org.sibadi.auditing.db
import org.sibadi.auditing.domain._
import org.sibadi.auditing.domain.errors.AppError

import java.util.UUID

class GroupService[F[_]](
  groupDao: db.GroupDAO[F],
  kpiDAO: db.KpiDAO[F],
  kpiGroupDao: db.KpiGroupDAO[F],
  teacherGroupDAO: db.TeacherGroupDAO[F],
  teacherDao: db.TeacherDAO[F]
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
          kpiLinks <- kpiGroupDao.getByGroupId(group.id)
          kpis <- kpiLinks
            .map { link =>
              kpiDAO.get(link.kpiId)
            }
            .flatTraverse(_.map(_.toList))
          teacherLinks <- teacherGroupDAO.getByGroupId(group.id)
          teachers <- teacherLinks
            .map { link =>
              teacherDao.getTeacherById(link.teacherId)
            }
            .flatTraverse(_.map(_.toList))
        } yield FullGroup(
          group.id,
          group.title,
          kpis.map(kpi => FullKpi(kpi.id, kpi.title)),
          teachers.map(teacher => FullTeacher(teacher.id, teacher.firstName, teacher.lastName, teacher.middleName))
        )
      }.sequence
    } yield fullGroups

    EitherT(
      dbLogic
        .map(_.asRight[AppError])
        .handleError(throwable => AppError.Unexpected(throwable).asLeft[List[FullGroup]])
    )
  }

  def addKpiToGroup(groupId: String, kpiId: String): EitherT[F, AppError, Unit] = {

    def linkF: EitherT[F, AppError, Unit] = EitherT(
      kpiGroupDao.insert(db.KpiGroup(kpiId, groupId)).map(_.asRight[AppError]).handleError(throwable => AppError.Unexpected(throwable).asLeft[Unit])
    )

    for {
      _ <- getGroupF(groupId) // check group exists
      _ <- getKpiF(kpiId) // check kpi exists
      _ <- linkF
    } yield ()
  }

  private def getGroupF(groupId: String): EitherT[F, AppError, db.Group] =
    OptionT(groupDao.get(groupId).handleError(_ => None)).toRight(AppError.GroupDoesNotExists(groupId).cast)

  private def getKpiF(kpiId: String): EitherT[F, AppError, db.Kpi] =
    OptionT(kpiDAO.get(kpiId).handleError(_ => None)).toRight(AppError.KpiDoesNotExists(kpiId).cast)

  def removeKpiFromGroup(groupId: String, kpiId: String): EitherT[F, AppError, Unit] = {

    def removeLinkF(): EitherT[F, AppError, Unit] = EitherT(
      kpiGroupDao.delete(db.KpiGroup(kpiId, groupId)).map(_.asRight[AppError]).handleError(throwable => AppError.Unexpected(throwable).asLeft[Unit])
    )

    for {
      _ <- getGroupF(groupId) // check group exists
      _ <- getKpiF(kpiId) // check kpi exists
      _ <- removeLinkF()
    } yield ()
  }

  def addTeacherToGroup(groupId: String, teacherId: String): EitherT[F, AppError, Unit] = {

    def linkF: EitherT[F, AppError, Unit] = EitherT(
      teacherGroupDAO
        .insert(db.TeacherGroup(teacherId, groupId))
        .map(_.asRight[AppError])
        .handleError(throwable => AppError.Unexpected(throwable).asLeft[Unit])
    )

    for {
      _ <- getGroupF(groupId) // check group exists
      _ <- getTeacherF(teacherId) // check teacher exists
      _ <- linkF
    } yield ()
  }

  private def getTeacherF(teacherId: String): EitherT[F, AppError, db.Teacher] =
    OptionT(teacherDao.getTeacherById(teacherId).handleError(_ => None)).toRight(AppError.TeacherDoesNotExists(teacherId).cast)

  def removeTeacherFromGroup(groupId: String, teacherId: String): EitherT[F, AppError, Unit] = {

    def removeLinkF(): EitherT[F, AppError, Unit] = EitherT(
      teacherGroupDAO
        .delete(db.TeacherGroup(teacherId, groupId))
        .map(_.asRight[AppError])
        .handleError(throwable => AppError.Unexpected(throwable).asLeft[Unit])
    )

    for {
      _ <- getGroupF(groupId) // check group exists
      _ <- getTeacherF(teacherId) // check teacher exists
      _ <- removeLinkF()
    } yield ()
  }

  def deleteGroup(groupId: String, title: String): EitherT[F, AppError, Unit] = {

    def removeLinkF(): EitherT[F, AppError, Unit] = EitherT(
      groupDao
        .deleteGroup(db.Group(groupId, title, None))
        .map(_.asRight[AppError])
        .handleError(throwable => AppError.Unexpected(throwable).asLeft[Unit])
    )

    for {
      _ <- getGroupF(groupId) // check group exists
      _ <- removeLinkF()
    } yield ()
  }

}

object GroupService {

  def apply[F[_]](
    groupDao: db.GroupDAO[F],
    kpiDAO: db.KpiDAO[F],
    kpiGroupDao: db.KpiGroupDAO[F],
    teacherGroupDAO: db.TeacherGroupDAO[F],
    teacherDao: db.TeacherDAO[F]
  )(implicit
    M: MonadCancel[F, Throwable]
  ): Resource[F, GroupService[F]] =
    Resource.pure(new GroupService(groupDao, kpiDAO, kpiGroupDao, teacherGroupDAO, teacherDao))

}
