package org.sibadi.auditing.api.routes

import cats.effect.Sync
import cats.syntax.applicativeError._
import org.sibadi.auditing.api.endpoints.GroupsAPI._
import org.sibadi.auditing.api.model._
import org.sibadi.auditing.service._
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

class GroupsRouter[F[_]: Sync](
  authenticator: Authenticator[F],
  estimateService: EstimateService[F],
  groupService: GroupService[F],
  kpiService: KpiService[F],
  reviewerService: ReviewerService[F],
  teacherService: TeacherService[F],
  topicService: TopicService[F]
) {

  implicit private def logger: Logger[F] = Slf4jLogger.getLogger

  implicit private val auth: Authenticator[F] = authenticator

  def routes =
    List(
      adminCreateGroups,
      adminGetGroups,
      adminAddKpiToGroup,
      adminDeleteKpiFromGroup,
      adminAddTeacherToGroup,
      adminDeleteTeacherFromGroup,
      adminDeleteGroup
    )

  private def adminCreateGroups =
    postApiAdminGroups
      .serverSecurityLogic(adminSecurityLogic[F])
      .serverLogic { _ => body =>
        groupService
          .createGroup(title = body.name)
          .leftSemiflatMap(toApiError[F])
          .value
          .handleErrorWith(throwableToUnexpected[F, Unit])
      }

  private def adminGetGroups =
    getApiAdminGroups
      .serverSecurityLogic(adminSecurityLogic[F])
      .serverLogic { _ => _ =>
        groupService.getAllGroups
          .leftSemiflatMap(toApiError[F])
          .map(_.map { group =>
            val kpis     = group.kpis.map(kpi => KpiInGroupItemDto(kpi.id, kpi.title))
            val teachers = group.teachers.map(teacher => TeacherInGroupItemDto(teacher.id, teacher.firstName, teacher.lastName, teacher.middleName))
            GroupItemResponseDto(group.id, group.title, kpis, teachers)
          })
          .value
          .handleErrorWith(throwableToUnexpected[F, List[GroupItemResponseDto]])
      }

  private def adminAddKpiToGroup =
    putApiAdminGroupsGroupIdKpiKpiId
      .serverSecurityLogic(adminSecurityLogic[F])
      .serverLogic { _ => body =>
        groupService
          .addKpiToGroup(body._1, body._2)
          .leftSemiflatMap(toApiError[F])
          .value
          .handleErrorWith(throwableToUnexpected[F, Unit])
      }

  private def adminDeleteKpiFromGroup =
    deleteApiAdminGroupsGroupIdKpiKpiId
      .serverSecurityLogic(adminSecurityLogic[F])
      .serverLogic { _ => body =>
        groupService
          .removeKpiFromGroup(body._1, body._2)
          .leftSemiflatMap(toApiError[F])
          .value
          .handleErrorWith(throwableToUnexpected[F, Unit])
      }

  private def adminAddTeacherToGroup =
    putApiAdminGroupsGroupIdTeacherTeacherId
      .serverSecurityLogic(adminSecurityLogic[F])
      .serverLogic { _ => body =>
        groupService
          .addTeacherToGroup(body._1, body._2)
          .leftSemiflatMap(toApiError[F])
          .value
          .handleErrorWith(throwableToUnexpected[F, Unit])
      }

  private def adminDeleteTeacherFromGroup =
    deleteApiAdminGroupsGroupIdTeacherTeacherId
      .serverSecurityLogic(adminSecurityLogic[F])
      .serverLogic { _ => body =>
        groupService
          .removeTeacherFromGroup(body._1, body._2)
          .leftSemiflatMap(toApiError[F])
          .value
          .handleErrorWith(throwableToUnexpected[F, Unit])
      }

  private def adminDeleteGroup =
    deleteApiAdminGroupsGroupId
      .serverSecurityLogic(adminSecurityLogic[F])
      .serverLogic { _ => body =>
        groupService
          .deleteGroup(body._1, body._2)
          .leftSemiflatMap(toApiError[F])
          .value
          .handleErrorWith(throwableToUnexpected[F, Unit])
      }

}
