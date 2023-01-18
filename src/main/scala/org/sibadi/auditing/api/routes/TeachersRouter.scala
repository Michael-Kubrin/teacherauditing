package org.sibadi.auditing.api.routes

import cats.effect.Sync
import cats.syntax.all._
import org.sibadi.auditing.api.endpoints.TeachersAPI._
import org.sibadi.auditing.api.model.{ApiError, ResponseIdPassword, TeacherResponse, toApiError}
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

  private implicit def logger: Logger[F] = Slf4jLogger.getLogger

  def routes = List(adminCreateTeacher, adminGetTeachers, adminEditTeacher)

  private def adminCreateTeacher =
    postApiAdminTeachers
      .serverSecurityLogic { token =>
        authenticator.isAdmin(token)
          .toRight(ApiError.Unauthorized(s"Unauthorized by token: $token").cast)
          .value.handleErrorWith(throwableToUnexpected[F, Authenticator.UserType])
      }
      .serverLogic { userType => body =>
        teacherService
          .createTeacher(
            firstName = body.name,
            lastName = body.surName,
            middleName = body.middleName,
            login = body.login
          )
          .map { createdTeacher =>
            ResponseIdPassword(createdTeacher.id, createdTeacher.password)
          }
          .leftSemiflatMap(toApiError[F])
          .value.handleErrorWith(throwableToUnexpected[F, ResponseIdPassword])
      }

  private def adminGetTeachers =
    getApiAdminTeachers
      .serverSecurityLogic { bearer =>
        authenticator.isAdmin(bearer).toRight(ApiError.Unauthorized("Unauthorized").cast).value.handleErrorWith(throwableToUnexpected[F, Authenticator.UserType])
      }
      .serverLogic { userType => body =>
//        teacherService.getAllTeachers
//          .leftSemiflatMap(toApiError[F])
//          TODO: how to set groupNames?)
//          .map(_.map(x => TeacherResponse(x.id, x.firstName, x.lastName, x.middleName, groupNames = ???)))
//          .value.handleErrorWith(throwableToUnexpected[F, Unit])
        ApiError.InternalError("Not implemented").cast.asLeft[List[TeacherResponse]].pure[F]
      }

  private def adminEditTeacher =
    putApiAdminTeachers
      .serverSecurityLogic { token =>
        authenticator.isAdmin(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value.handleErrorWith(throwableToUnexpected[F, Authenticator.UserType])
      }
      .serverLogic { userType => body =>
        teacherService
          .updateTeacher(body._2.name, body._2.surName, body._2.middleName, body._1)
          .leftSemiflatMap(toApiError[F])
          .value.handleErrorWith(throwableToUnexpected[F, Unit])
      }

}
