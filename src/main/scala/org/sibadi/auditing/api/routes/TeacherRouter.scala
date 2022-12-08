package org.sibadi.auditing.api.routes

import cats.syntax.all._

import cats.Monad
import org.sibadi.auditing.api.endpoints.TeachersAPI._
import org.sibadi.auditing.api.model.{toApiError, ApiError, ResponseIdPassword}
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
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
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
          .leftMap { case AppError.Unexpected(_) =>
            ApiError.InternalError("Cannot create teacher").cast
          }
          .value
      }

  private def adminGetTeachers =
    getApiAdminTeachers
      .serverSecurityLogic { bearer =>
        authenticator.authenticate(bearer).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType => body =>
        for {
          teachers <- teacherService.getAllTeachers
        } yield teachers
      }

  //TODO: It's not right
  private def adminEditTeacher =
    putApiAdminTeachers
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType => body =>
        for {
          takeData <- teacherService.getAllTeachers
          firstName  = takeData.map(teacher => teacher.firstName)
          lastName   = takeData.map(teacher => teacher.lastName)
          middleName = takeData.map(teacher => teacher.middleName)
          teacherId  = takeData.map(teacher => teacher.id)
          teachers <- teacherService.updateTeacher(firstName, lastName, middleName, teacherId)
        } yield teachers
      }

}
