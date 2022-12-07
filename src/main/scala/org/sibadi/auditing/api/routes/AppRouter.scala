package org.sibadi.auditing.api.routes

import cats.effect.{Async, Resource}
import cats.syntax.all._
import org.http4s.HttpRoutes
import org.sibadi.auditing.api.endpoints.GeneratedEndpoints
import org.sibadi.auditing.api.endpoints.GeneratedEndpoints._
import org.sibadi.auditing.api.model.ApiError
import org.sibadi.auditing.domain.errors.AppError
import org.sibadi.auditing.service.{Authenticator, EstimateService, ReviewerService, Service, TeacherService, TopicKpiService, TopicService}
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter

class AppRouter[F[_]: Async](
  authenticator: Authenticator[F],
  service: Service[F],
  teacherService: TeacherService[F],
  reviewerService: ReviewerService[F],
  topicService: TopicService[F],
  topicKpiService: TopicKpiService[F],
  estimateService: EstimateService[F]
) {

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
    adminCreateReviewers,
    adminGetReviewers,
    adminEditReviewers,
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
        authenticator.isAdmin(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { admin => body =>
        val topics = body.topics.map(dto => (dto.title, dto.kpis.map(_.title).toSet)).toMap
        topicService.createTopics(topics)
          .leftMap(toApiError)
          .value
      }

  // TODO implement
  private def toApiError(appError: AppError): ApiError = ???

  private def adminGetTopic =
    getApiAdminTopics
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        topicService
          .getTopic(groupId = ???)
          .map { topic =>
            topic.id
          }
          .leftMap { case AppError.TeacherDoesNotExists(_) =>
            ApiError.BadRequest("Cannot find topic").cast
          }
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
        topicService
          .updateTopic(
            kpis = ???,
            groupId = ???,
            title = ???
          )
          .leftMap { case AppError.Unexpected(_) =>
            ApiError.InternalError("Cannot edit teacher").cast
          }
          .value
      }

  private def adminCreateTopicKpi =
    postApiAdminTopicsTopicIdKpi
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        topicKpiService
          .createTopicKpi(
            kpiId = ???,
            topicId = ???
          )
          .map { response =>
            ResponseId(response.kpiId)
          }
          .leftMap { case AppError.Unexpected(_) =>
            ApiError.InternalError("Cannot create KPI").cast
          }
          .value
      }

  private def adminGetTopicKpi =
    getApiAdminTopicsTopicIdKpi
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        topicKpiService
          .getTopicKpiByKpiId(
            kpiId = ???
          )
          .map { response =>
            TopicKpiResponse(response.topicId, response.topicId)
          }
          .leftMap { case AppError.Unexpected(_) =>
            ApiError.InternalError("Cannot get KPI").cast
          }
          .value
      }

  private def adminEditTopicKpiId =
    putApiAdminTopicsTopicIdKpiKpiId
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        topicKpiService
          .editTopicKpi(
            kpiId = ???,
            topicId = ???
          )
          .leftMap { case AppError.Unexpected(_) =>
            ApiError.InternalError("Cannot edit KPI").cast
          }
          .value
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

  private def adminGetTeacher =
    getApiAdminTeachers
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        teacherService
          .getTeacher(teacherId = ???)
          .map { response =>
            response.id
          }
          .leftMap { case AppError.TeacherDoesNotExists(_) =>
            ApiError.BadRequest("Cannot find teacher").cast
          }
      }

  //TODO: It's not right
  private def adminEditTeacher =
    putApiAdminTeachers
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        teacherService
          .updateTeacher(
            firstName = ???,
            lastName = ???,
            middleName = ???,
            login = ???,
            teacherId = ???
          )
          .leftMap { case AppError.Unexpected(_) =>
            ApiError.InternalError("Cannot edit teacher").cast
          }
          .value
      }

  private def adminCreateReviewers =
    postApiAdminReviewers
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        reviewerService
          .createReviewer(
            firstName = body.name,
            lastName = body.surName,
            middleName = body.middleName,
            login = body.login
          )
          .map { createdReviewer =>
            ResponseIdPassword(createdReviewer.id, createdReviewer.password)
          }
          .leftMap { case AppError.Unexpected(_) =>
            ApiError.InternalError("Cannot create reviewer").cast
          }
          .value
      }

  private def adminGetReviewers =
    getApiAdminReviewers
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        reviewerService
          .getReviewer(reviewerId = ???)
          .map { response =>
            response.id
          }
          .leftMap { case AppError.TeacherDoesNotExists(_) =>
            ApiError.BadRequest("Cannot find reviewer").cast
          }
      }

  private def adminEditReviewers =
    putApiAdminReviewersId
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        reviewerService
          .updateReviewer(
            firstName = ???,
            lastName = ???,
            middleName = ???,
            login = ???,
            reviewerId = ???
          )
          .leftMap { case AppError.Unexpected(_) =>
            ApiError.InternalError("Cannot edit reviewer").cast
          }
          .value
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
