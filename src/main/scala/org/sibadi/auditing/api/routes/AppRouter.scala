package org.sibadi.auditing.api.routes

import cats.effect.{Async, Resource}
import cats.syntax.all._
import org.http4s.HttpRoutes
import org.sibadi.auditing.api.endpoints.GeneratedEndpoints
import org.sibadi.auditing.api.endpoints.GeneratedEndpoints._
import org.sibadi.auditing.api.model.ApiError
import org.sibadi.auditing.domain.errors.AppError
import org.sibadi.auditing.service.{Authenticator, Service}
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter

class AppRouter[F[_]: Async](authenticator: Authenticator[F], service: Service[F]) {

  private val docRoutes: List[ServerEndpoint[Any, F]] = SwaggerInterpreter().fromEndpoints[F](GeneratedEndpoints.allEndpoints, "aboba", "1.0.0")

  def httpRoutes: HttpRoutes[F] = Http4sServerInterpreter[F]().toRoutes(allRoutes ++ docRoutes)

  private def allRoutes: List[ServerEndpoint[Any, F]] = List(
    adminCreateTopic,
    adminGetTopic,
    adminDeleteTopic,
    adminEditTopicById,
    adminCreateTopicKpi,
    adminGetTopicKpi,
    adminEditTopicKpiId,
    adminDeleteTopicKpiId,
    adminEditStatus,
    adminCreateTeacher,
    adminGetTeacher,
    adminEditTeacher,
    adminCreateRivewers,
    adminGetRivewers,
    adminEditRivewers,
    adminCreateTeacherId,
    adminDeleteTeacherId,
    publicGetTopics,
    publicGetKpi,
    publicEstimate,
    adminCreateGroups,
    adminGetGroups,
    adminEditGroups,
    adminDeleteGropus,
    createLogin,
    changePassword
  )

  private def adminCreateTopic =
    postApiAdminTopics
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[ResponseId].pure[F]
      }

  private def adminGetTopic =
    getApiAdminTopics
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[List[TopicItemResponseDto]].pure[F]
      }

  private def adminDeleteTopic =
    deleteApiAdminTopics
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[String].pure[F] //TODO: Not sure about it
      }

  private def adminEditTopicById =
    putApiAdminTopicsTopicId
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[String].pure[F]
      }

  private def adminCreateTopicKpi =
    postApiAdminTopicsTopicIdKpi
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[ResponseId].pure[F]
      }

  private def adminGetTopicKpi =
    getApiAdminTopicsTopicIdKpi
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[List[TopicKpiResponse]].pure[F]
      }

  private def adminEditTopicKpiId =
    putApiAdminTopicsTopicIdKpiKpiId
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[String].pure[F]
      }

  private def adminDeleteTopicKpiId =
    deleteApiAdminTopicsTopicIdKpiKpiId
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[String].pure[F]
      }

  private def adminEditStatus =
    putApiAdminTopicsTopicIdKpiKpiIdTeachersTeacherIdStatus
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[String].pure[F]
      }

  private def adminCreateTeacher =
    postApiAdminTeachers
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        service.createTeacher(
          firstName = body.name,
          lastName = body.surName,
          middleName = body.middleName,
          login = body.login
        )
          .map { createdTeacher =>
            ResponseIdPassword(createdTeacher.id, createdTeacher.password)
          }
          .leftMap {
            case AppError.Unexpected(_) => ApiError.InternalError("Cannot create teacher").cast
          }
          .value
      }

  private def adminGetTeacher =
    getApiAdminTeachers
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[List[TeacherResponse]].pure[F]
      }

  private def adminEditTeacher =
    putApiAdminTeachers
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[String].pure[F]
      }

  private def adminCreateRivewers =
    postApiAdminReviewers
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[ResponseIdPassword].pure[F]
      }

  private def adminGetRivewers =
    getApiAdminReviewers
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[List[ReviewerResponse]].pure[F]
      }

  private def adminEditRivewers =
    putApiAdminReviewersId
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[String].pure[F]
      }

  private def adminCreateTeacherId =
    postApiAdminTopicsTopicIdKpiKpiIdTeacherTeacherId
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[String].pure[F]
      }

  private def adminDeleteTeacherId =
    deleteApiAdminTopicsTopicIdKpiKpiIdTeacherTeacherId
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[String].pure[F]
      }

  private def publicGetTopics =
    getApiPublicTopics
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[TopicItemResponseDto].pure[F]
      }

  private def publicGetKpi =
    getApiPublicTopicsTopicIdKpi
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[GetPublicKpiResponse].pure[F]
      }

  private def publicEstimate =
    postApiPublicTopicsTopicIdKpiKpiIdEstimate
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[ResponseId].pure[F]
      }

  private def adminCreateGroups =
    postApiAdminGroups
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[String].pure[F]
      }

  private def adminGetGroups =
    getApiAdminGroups
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[GroupsResponse].pure[F]
      }

//  def publicUploadFile =
//    postApiPublicTopicsTopicIdKpiKpiIdFiles
//      .serverSecurityLogic { token =>
//        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
//      }
//      .serverLogic { userType => body =>
//        ApiError.InternalError("Not implemented").cast.asLeft[ResponseId].pure[F]
//      }
//
//  def publicUploadFileId =
//    postApiPublicTopicsTopicIdKpiKpiIdFilesFileId
//      .serverSecurityLogic { token =>
//        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
//      }
//      .serverLogic { userType =>
//        body =>
//          ApiError.InternalError("Not implemented").cast.asLeft[].pure[F]
//      }

  private def adminEditGroups =
    putApiAdminGroupsGroupIdTopicsTopicIdKpiKpiId
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[String].pure[F]
      }

  private def adminDeleteGropus =
    deleteApiAdminGroupsGroupIdTopicsTopicIdKpiKpiId
      .serverSecurityLogic { token =>
        authenticator
          .isAdmin(token)
          .toRight(ApiError.Unauthorized("Unauthorized").cast)
          .value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[String].pure[F]
      }

  private def createLogin =
    postLogin
      .serverLogic { userType =>
        ApiError.InternalError("Not implemented").cast.asLeft[LoginResponse].pure[F]
      }

  private def changePassword =
    editPassword
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[PasswordResponse].pure[F]
      }
}

object AppRouter {
  def apply[F[_]: Async](authenticator: Authenticator[F], service: Service[F]): Resource[F, AppRouter[F]] =
    Resource.pure(new AppRouter(authenticator, service))
}
