package org.sibadi.auditing.api.routes

import cats.effect.Sync
import cats.syntax.applicativeError._
import org.sibadi.auditing.api.endpoints.TeachersAPI._
import org.sibadi.auditing.api.model._
import org.sibadi.auditing.service._
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

class TeachersRouter[F[_]: Sync](
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

  def routes = List(adminCreateTeacher, adminGetTeachers, adminEditTeacher, adminGetTeacher)

  private def adminCreateTeacher =
    postApiAdminTeachers
      .serverSecurityLogic(adminSecurityLogic[F])
      .serverLogic { _ => body =>
        teacherService
          .createTeacher(
            firstName = body.name,
            lastName = body.surName,
            middleName = body.middleName,
            login = body.login
          )
          .map { createdTeacher =>
            CredentialsResponseDto(createdTeacher.id, createdTeacher.password)
          }
          .leftSemiflatMap(toApiError[F])
          .value
          .handleErrorWith(throwableToUnexpected[F, CredentialsResponseDto])
      }

  private def adminGetTeachers =
    getApiAdminTeachers
      .serverSecurityLogic(adminSecurityLogic[F])
      .serverLogic { _ => _ =>
        teacherService.getAllTeachers
          .map(_.map(teacher => TeacherItemResponseDto(teacher.id, teacher.firstName, teacher.lastName, teacher.middleName)))
          .leftSemiflatMap(toApiError[F])
          .value
          .handleErrorWith(throwableToUnexpected[F, List[TeacherItemResponseDto]])
      }

  private def adminGetTeacher =
    getApiAdminTeachersTeacherId
      .serverSecurityLogic(adminSecurityLogic[F])
      .serverLogic { _ => teacherId =>
        teacherService
          .getTeacher(teacherId)
          .map(teacher =>
            TeacherResponse(
              teacher.id,
              teacher.firstName,
              teacher.lastName,
              teacher.middleName,
              teacher.groups.map(group => TeacherGroupItemResponse(group.id, group.name))
            )
          )
          .leftSemiflatMap(toApiError[F])
          .value
          .handleErrorWith(throwableToUnexpected[F, TeacherResponse])
      }

  private def adminEditTeacher =
    putApiAdminTeachers
      .serverSecurityLogic(adminSecurityLogic[F])
      .serverLogic { _ => body =>
        teacherService
          .updateTeacher(body._2.name, body._2.surName, body._2.middleName, body._1)
          .leftSemiflatMap(toApiError[F])
          .value
          .handleErrorWith(throwableToUnexpected[F, Unit])
      }

}
