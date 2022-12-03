package org.sibadi.auditing.routes

import cats.Monad
import cats.syntax.all._
import org.sibadi.auditing.domain.ApiError
import org.sibadi.auditing.endpoints.GeneratedEndpoints._
import org.sibadi.auditing.service.Authenticator

class Router[F[_]: Monad](authenticator: Authenticator[F]) extends RouterOps[F] {

  // TODO handle all endpoints with 500 return

  def routes = List(
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
    adminDeleteGropus
  )

  def createLogin =
    postLogin
      .serverLogic { userType =>
        ApiError.InternalError("Not implemented").cast.asLeft[LoginResponse].pure[F]
      }

  def changePassword =
    editPassword
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[PasswordResponse].pure[F]
      }

  def adminCreateTopic =
    postApiAdminTopics
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[ResponseId].pure[F]
      }

  def adminGetTopic =
    getApiAdminTopics
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[List[TopicItemResponseDto]].pure[F]
      }

  def adminDeleteTopic =
    deleteApiAdminTopics
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[String].pure[F] //TODO: Not sure about it
      }

  def adminEditTopicById =
    putApiAdminTopicsTopicId
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[String].pure[F]
      }

  def adminCreateTopicKpi =
    postApiAdminTopicsTopicIdKpi
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[ResponseId].pure[F]
      }

  def adminGetTopicKpi =
    getApiAdminTopicsTopicIdKpi
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[List[TopicKpiResponse]].pure[F]
      }

  def adminEditTopicKpiId =
    putApiAdminTopicsTopicIdKpiKpiId
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[String].pure[F]
      }

  def adminDeleteTopicKpiId =
    deleteApiAdminTopicsTopicIdKpiKpiId
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[String].pure[F]
      }

  def adminEditStatus =
    putApiAdminTopicsTopicIdKpiKpiIdTeachersTeacherIdStatus
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[String].pure[F]
      }

  def adminCreateTeacher =
    postApiAdminTeachers
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[ResponseIdPassword].pure[F]
      }

  def adminGetTeacher =
    getApiAdminTeachers
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[List[TeacherResponse]].pure[F]
      }

  def adminEditTeacher =
    putApiAdminTeachers
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[String].pure[F]
      }

  def adminCreateRivewers =
    postApiAdminReviewers
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[ResponseIdPassword].pure[F]
      }

  def adminGetRivewers =
    getApiAdminReviewers
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[List[ReviewerResponse]].pure[F]
      }

  def adminEditRivewers =
    putApiAdminReviewersId
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[String].pure[F]
      }

  def adminCreateTeacherId =
    postApiAdminTopicsTopicIdKpiKpiIdTeacherTeacherId
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[String].pure[F]
      }

  def adminDeleteTeacherId =
    deleteApiAdminTopicsTopicIdKpiKpiIdTeacherTeacherId
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[String].pure[F]
      }

  def publicGetTopics =
    getApiPublicTopics
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[TopicItemResponseDto].pure[F]
      }

  def publicGetKpi =
    getApiPublicTopicsTopicIdKpi
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[GetPublicKpiResponse].pure[F]
      }

  def publicEstimate =
    postApiPublicTopicsTopicIdKpiKpiIdEstimate
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[ResponseId].pure[F]
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

  def adminCreateGroups =
    postApiAdminGroups
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[String].pure[F]
      }

  def adminGetGroups =
    getApiAdminGroups
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[GroupsResponse].pure[F]
      }

  def adminEditGroups =
    putApiAdminGroupsGroupIdTopicsTopicIdKpiKpiId
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[String].pure[F]
      }

  def adminDeleteGropus =
    deleteApiAdminGroupsGroupIdTopicsTopicIdKpiKpiId
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value // TODO check valid userType
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[String].pure[F]
      }
}
