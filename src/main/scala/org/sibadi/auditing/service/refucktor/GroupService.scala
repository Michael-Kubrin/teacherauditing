package org.sibadi.auditing.service.refucktor

import cats.data.EitherT
import cats.effect.MonadCancel
import cats.effect.std.UUIDGen
import cats.syntax.eq._
import cats.syntax.traverse._
import doobie.syntax.all._
import doobie.util.transactor.Transactor
import org.sibadi.auditing.api.endpoints._
import org.sibadi.auditing.api.endpoints.model._
import org.sibadi.auditing.db.model.{GroupDbModel, KpiDbModel, TeacherDbModel}
import org.sibadi.auditing.syntax.sql._
import org.typelevel.log4cats.Logger
import doobie.postgres.implicits.JavaTimeLocalDateTimeMeta

class GroupService[F[_]: UUIDGen: Logger](
  transactor: Transactor[F]
)(implicit M: MonadCancel[F, Throwable]) {

  def createGroupEndpointHandle(params: CreateGroupRequestDto): EitherT[F, ApiError, Unit] =
    for {
      name <- EitherT.cond(!params.name.isBlank, params.name, emptyNameError)
      uuid <- EitherT.liftF(UUIDGen.randomString)
      _    <- sql"""INSERT INTO "group" (id, title) VALUES ($uuid, $name)""".update.run.eitherT(transactor)
    } yield ()

  def deleteGroupEndpointHandle(groupId: String): EitherT[F, ApiError, Unit] =
    for {
      anyRowExists <- sql"""SELECT 1 FROM "group" WHERE id = $groupId""".query[Int].option.eitherT(transactor).map(_.isDefined)
      _            <- EitherT.cond(anyRowExists, (), noGroupWithGivenId)
      _            <- sql"""DELETE FROM "group" WHERE id = $groupId""".update.run.eitherT(transactor)
    } yield ()

  def editGroupEndpointHandle(groupId: String, body: EditGroupRequestDto): EitherT[F, ApiError, Unit] =
    for {
      nonEmptyName     <- EitherT.cond(!body.name.isBlank, body.name, emptyNameError)
      group            <- sql"""SELECT title FROM "group" WHERE id = $groupId""".query[String].option.eitherT(transactor)
      groupCurrentName <- EitherT.fromOption(group, noGroupWithGivenId)
      _                <- EitherT.cond(groupCurrentName =!= nonEmptyName, (), sameNameError)
      _                <- sql"""UPDATE "group" SET title = $nonEmptyName WHERE id = $groupId""".update.run.eitherT(transactor)
    } yield ()

  def getAllGroupsEndpointHandle: EitherT[F, ApiError, List[GroupItemResponseDto]] = {

    def getGroupKpis(groupId: String) =
      sql"""
       SELECT kpi.id as id, kpi.title as title, kpi.deleteDt as deleteDt
       FROM kpi_group
       INNER JOIN kpi ON kpi.id = kpiId
       WHERE groupId = $groupId AND kpi.deleteDt IS NULL
       """.query[KpiDbModel].to[List].eitherT(transactor)

    def getGroupTeachers(groupId: String) =
      sql"""
       SELECT t.id as id, t.firstName as firstName, t.lastName as lastName, t.middleName as middleName, t.deleteDt as deleteDt
       FROM teacher_group tg
       INNER JOIN teacher t ON t.id = tg.teacherId
       WHERE tg.groupId = $groupId AND t.deleteDt IS NULL
       """.query[TeacherDbModel].to[List].eitherT(transactor)

    def getGroupFull(group: GroupDbModel) =
      for {
        kpis     <- getGroupKpis(group.id)
        teachers <- getGroupTeachers(group.id)
      } yield GroupItemResponseDto(
        id = group.id,
        name = group.title,
        kpis = kpis.map(model => KpiInGroupItemDto(model.id, model.title)),
        teachers = teachers.map(model => TeacherInGroupItemDto(model.id, model.firstName, model.lastName, model.middleName))
      )

    for {
      groups     <- sql"""SELECT id, title, deleteDt FROM "group" WHERE deleteDt IS NULL""".query[GroupDbModel].to[List].eitherT(transactor)
      fullGroups <- groups.map(getGroupFull).sequence
    } yield fullGroups
  }

  def putApiAdminGroupsGroupIdKpiKpiIdHandle(groupId: String, kpiId: String): EitherT[F, ApiError, Unit] =
    for {
      groupOpt <- sql"""SELECT id, title, deleteDt FROM "group" WHERE id = $groupId""".query[GroupDbModel].option.eitherT(transactor)
      _        <- EitherT.fromOption(groupOpt, noGroupWithGivenId)
      kpiOpt   <- sql"""SELECT id, title, deleteDt FROM kpi WHERE id = $kpiId""".query[KpiDbModel].option.eitherT(transactor)
      _        <- EitherT.fromOption(kpiOpt, noKpiWithGivenId)
      _ <- sql"""
              INSERT INTO kpi_group (kpiId, groupId)
              SELECT $kpiId, $groupId
              WHERE NOT EXISTS (SELECT 1 FROM kpi_group WHERE kpiId = $kpiId and groupId = $groupId)
              """.update.run.eitherT(transactor)
    } yield ()

  def putApiAdminGroupsGroupIdTeacherTeacherIdHandle(groupId: String, teacherId: String): EitherT[F, ApiError, Unit] =
    for {
      groupOpt   <- sql"""SELECT id, title, deleteDt FROM "group" WHERE id = $groupId""".query[GroupDbModel].option.eitherT(transactor)
      _          <- EitherT.fromOption(groupOpt, noGroupWithGivenId)
      teacherOpt <- sql"""SELECT id, firstName, lastName, middleName, deleteDt FROM kpi WHERE id = $groupId""".query[TeacherDbModel].option.eitherT(transactor)
      _          <- EitherT.fromOption(teacherOpt, noTeacherWithGivenId)
      _ <- sql"""
              INSERT INTO teacher_group (teacherId, groupId)
              SELECT $teacherId, $groupId
              WHERE NOT EXISTS (SELECT 1 FROM teacher_group WHERE teacherId = $teacherId and groupId = $groupId)
              """.update.run.eitherT(transactor)
    } yield ()

  def deleteApiAdminGroupsGroupIdKpiKpiIdHandle(groupId: String, kpiId: String): EitherT[F, ApiError, Unit] =
    for {
      groupOpt <- sql"""SELECT id, title, deleteDt FROM "group" WHERE id = $groupId""".query[GroupDbModel].option.eitherT(transactor)
      _        <- EitherT.fromOption(groupOpt, noGroupWithGivenId)
      kpiOpt   <- sql"""SELECT id, title, deleteDt FROM kpi WHERE id = $kpiId""".query[KpiDbModel].option.eitherT(transactor)
      _        <- EitherT.fromOption(kpiOpt, noKpiWithGivenId)
      _        <- sql"""DELETE FROM kpi_group WHERE kpiId = $kpiId and groupId = $groupId""".update.run.eitherT(transactor)
    } yield ()

  def deleteApiAdminGroupsGroupIdTeacherTeacherIdHandle(groupId: String, teacherId: String): EitherT[F, ApiError, Unit] =
    for {
      groupOpt   <- sql"""SELECT id, title, deleteDt FROM "group" WHERE id = $groupId""".query[GroupDbModel].option.eitherT(transactor)
      _          <- EitherT.fromOption(groupOpt, noGroupWithGivenId)
      teacherOpt <- sql"""SELECT id, firstName, lastName, middleName, deleteDt FROM kpi WHERE id = $groupId""".query[TeacherDbModel].option.eitherT(transactor)
      _          <- EitherT.fromOption(teacherOpt, noTeacherWithGivenId)
      _          <- sql"""DELETE FROM teacher_group WHERE teacherId = $teacherId and groupId = $groupId""".update.run.eitherT(transactor)
    } yield ()

}
