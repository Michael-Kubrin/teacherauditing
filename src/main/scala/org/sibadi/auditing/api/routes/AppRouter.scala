package org.sibadi.auditing.api.routes

import cats.effect.{Async, Resource}
import cats.syntax.all._
import org.http4s.HttpRoutes
import org.sibadi.auditing.api.endpoints.GeneratedEndpoints
import org.sibadi.auditing.api.endpoints.GeneratedEndpoints._
import org.sibadi.auditing.api.model.ApiError
import org.sibadi.auditing.domain.errors.AppError
import org.sibadi.auditing.service.{Authenticator, EstimateService, GroupService, ReviewerService, Service, TeacherService, TopicService}
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter

class AppRouter[F[_]: Async](
  authenticator: Authenticator[F],
  service: Service[F],
  teacherService: TeacherService[F],
  reviewerService: ReviewerService[F],
  topicService: TopicService[F],
  estimateService: EstimateService[F],
  groupService: GroupService[F]
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
        topicService
          .createTopics(topics)
          .leftMap(toApiError)
          .value
      }

  private def toApiError(appError: AppError): ApiError =
    appError match {
      case t => ApiError.NotFound(t.toString)
    }

  private def adminGetTopic =
    getApiAdminTopics
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        topicService.getAllTopics
          .leftMap(toApiError)
          .value
      }

  private def adminDeleteTopic =
    deleteApiAdminTopics
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        topicService
          .deleteTopicKpi(
            topicId = ???,
            kpiId = ???
          )
          .leftMap(toApiError)
          .value
      }

  private def adminEditTopicById =
    putApiAdminTopicsTopicId
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        topicService
          .updateTopic(
            topicId = ???,
            topicToKpisMap = ???
          )
          .leftMap(toApiError)
          .value
      }

  private def adminCreateTopicKpi =
    postApiAdminTopicsTopicIdKpi
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        topicService
          .createTopicKpi()
          .leftMap(toApiError)
          .value
      }

  private def adminGetTopicKpi =
    getApiAdminTopicsTopicIdKpi
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        topicService
          .getTopicKpiByKpiId(kpiId = ???)
          .leftMap(toApiError)
          .value
      }

  private def adminEditTopicKpiId =
    putApiAdminTopicsTopicIdKpiKpiId
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        topicService
          .editTopicKpi(topicId = body._1, kpiId = body._2)
          .leftMap(toApiError)
          .value
      }

  private def adminDeleteTopicKpiId =
    deleteApiAdminTopicsTopicIdKpiKpiId
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        topicService
          .deleteTopicKpi(topicId = body._1, kpiId = body._2)
          .leftMap(toApiError)
          .value
      }

  private def adminEditStatus =
    putApiAdminTopicsTopicIdKpiKpiIdTeachersTeacherIdStatus
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        estimateService
          .updateEstimate(topicId = body._1, kpiId = body._2, teacherId = body._3)
          .leftMap(toApiError)
          .value
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
          .leftMap(toApiError)
          .value
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
            teacherId = ???
          )
          .leftMap(toApiError)
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
          .leftMap(toApiError)
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
          .leftMap(toApiError)
          .value
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
            reviewerId = ???
          )
          .leftMap(toApiError)
          .value
      }

  private def adminCreateTeacherId =
    postApiAdminTopicsTopicIdKpiKpiIdTeacherTeacherId
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        teacherService
          .createTeacher(firstName = body._1, lastName = body._2, middleName = body._3.some, login = ???)
          .leftMap(toApiError)
          .value
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
        topicService
          .getTopic(groupId = ???)
          .leftMap(toApiError)
          .value
      }

  private def publicGetKpi =
    getApiPublicTopicsTopicIdKpi
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        topicService
          .getTopicKpiByTopicId(topicId = ???)
          .leftMap(toApiError)
          .value
      }

  private def publicEstimate =
    postApiPublicTopicsTopicIdKpiKpiIdEstimate
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        estimateService
          .createEstimate(topicId = body._1, kpiId = body._2, teacherId = ???)
          .leftMap(toApiError)
          .value
      }

  private def adminCreateGroups =
    postApiAdminGroups
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        groupService
          .createGroup(title = body.name)
          .leftMap(toApiError)
          .value
      }

  private def adminGetGroups =
    getApiAdminGroups
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        groupService
          .getGroup(groupId = ???)
          .leftMap(toApiError)
          .value
      }

  def publicUploadFile =
    postApiPublicTopicsTopicIdKpiKpiIdFiles
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        estimateService
          .createEstimateFiles(file = ???)
          .leftMap(toApiError)
          .value
      }

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
        groupService
          .updateGroup(group = ???)
          .leftMap(toApiError)
          .value
      }

  private def adminDeleteGropus =
    deleteApiAdminGroupsGroupIdTopicsTopicIdKpiKpiId
      .serverSecurityLogic { token =>
        authenticator
          .isAdmin(token)
          .toRight(ApiError.Unauthorized("Unauthorized").cast).value
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
