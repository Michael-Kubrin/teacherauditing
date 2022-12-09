package org.sibadi.auditing.api.routes

import cats.syntax.all._
import cats.Monad
import org.sibadi.auditing.api.endpoints.TeachersAPI._
import org.sibadi.auditing.api.model.{ApiError, ResponseIdPassword, TeacherResponse, toApiError}
import org.sibadi.auditing.domain.errors.AppError
import org.sibadi.auditing.service.{Authenticator, TeacherService}

class TeacherRouter[F[_]: Monad](
  authenticator: Authenticator[F],
  teacherService: TeacherService[F]
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
      .serverLogic { userType =>
        body =>
          ApiError.InternalError("Not implemented").cast.asLeft[List[TeacherResponse]].pure[F]
      }

  private def adminEditTeacher =
    putApiAdminTeachers
      .serverSecurityLogic { token =>
        authenticator.isAdmin(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType =>
        body =>
          ApiError.InternalError("Not implemented").cast.asLeft[Unit].pure[F]
      }

}
