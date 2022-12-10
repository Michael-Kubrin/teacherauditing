package org.sibadi.auditing.api.routes

import cats.Monad
import cats.syntax.all._
import org.sibadi.auditing.api.endpoints.TeachersAPI._
import org.sibadi.auditing.api.model.{toApiError, ApiError, ResponseIdPassword, TeacherResponse}
import org.sibadi.auditing.service._

class TeachersRouter[F[_]: Monad](
  authenticator: Authenticator[F],
  estimateService: EstimateService[F],
  groupService: GroupService[F],
  kpiService: KpiService[F],
  reviewerService: ReviewerService[F],
  teacherService: TeacherService[F],
  topicService: TopicService[F]
) {

  def routes = List(adminCreateTeacher, adminGetTeachers, adminEditTeacher)

  private def adminCreateTeacher =
    postApiAdminTeachers
      .serverSecurityLogic { token =>
        authenticator.isAdmin(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
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
          .leftMap(toApiError)
          .value
      }

  private def adminGetTeachers =
    getApiAdminTeachers
      .serverSecurityLogic { bearer =>
        authenticator.isAdmin(bearer).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType => body =>
//        teacherService.getAllTeachers
//          .leftMap(toApiError)
//          TODO: how to set groupNames?)
//          .map(_.map(x => TeacherResponse(x.id, x.firstName, x.lastName, x.middleName, groupNames = ???)))
//          .value
        ApiError.InternalError("Not implemented").cast.asLeft[List[TeacherResponse]].pure[F]
      }

  private def adminEditTeacher =
    putApiAdminTeachers
      .serverSecurityLogic { token =>
        authenticator.isAdmin(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType => body =>
        teacherService
          .updateTeacher(body._2.name, body._2.surName, body._2.middleName, body._1)
          .leftMap(toApiError)
          .value
      }

}