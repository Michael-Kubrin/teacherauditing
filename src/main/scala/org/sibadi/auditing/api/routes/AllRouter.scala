package org.sibadi.auditing.api.routes

import cats.effect.Async
import org.sibadi.auditing.api.endpoints.AdminAPI._
import org.sibadi.auditing.api.endpoints.FullApi._
import org.sibadi.auditing.api.endpoints.model.BearerResponseDto
import org.sibadi.auditing.api.endpoints.unauthorized
import org.sibadi.auditing.service.refucktor.GroupService
import org.sibadi.auditing.service.{AllService, Authenticator}
import org.typelevel.log4cats.Logger

class AllRouter[F[_]: Async: Logger](
  service: AllService[F],
  groupService: GroupService[F],
  authenticator: Authenticator[F]
) {

  def all =
    List(
      createGroupEndpointLogic,
      getAllGroupsEndpointLogic,
      deleteGroupEndpointLogic,
      editGroupEndpointLogic,
      putApiAdminGroupsGroupIdTeacherTeacherIdLogic,
      deleteApiAdminGroupsGroupIdTeacherTeacherIdLogic,
      putApiAdminGroupsGroupIdKpiKpiIdLogic,
      deleteApiAdminGroupsGroupIdKpiKpiIdLogic,
      createTopicEndpointLogic,
      getAllTopicsEndpointLogic,
      deleteTopicEndpointLogic,
      editTopicNameEndpointLogic,
      createKpiEndpointLogic,
      getAllKpiEndpointLogic,
      editKpiEndpointLogic,
      deleteKpiEndpointLogic,
      createTeachersEndpointLogic,
      getTeachersEndpointLogic,
      editTeachersEndpointLogic,
      deleteTeachersEndpointLogic,
      createReviewersEndpointLogic,
      getAllReviewersEndpointLogic,
      editReviewersEndpointLogic,
      deleteReviewersEndpointLogic,
      loginEndpointLogic,
      getKpiDataForTeacherLogic,
      getKpisByTeacherForReviewerLogic,
      estimateTeacherEndpointLogic,
      fillKpiEndpointLogic
    )

  def createGroupEndpointLogic = createGroupEndpoint.serverLogic { params =>
    groupService.createGroupEndpointHandle(params).value
  }

  def getAllGroupsEndpointLogic = getAllGroupsEndpoint.serverLogic { _ =>
    groupService.getAllGroupsEndpointHandle.value
  }

  def deleteGroupEndpointLogic = deleteGroupEndpoint.serverLogic { groupId =>
    groupService.deleteGroupEndpointHandle(groupId).value
  }

  def editGroupEndpointLogic = editGroupEndpoint.serverLogic { case (groupId, body) =>
    groupService.editGroupEndpointHandle(groupId, body).value
  }

  def putApiAdminGroupsGroupIdTeacherTeacherIdLogic = putApiAdminGroupsGroupIdTeacherTeacherId.serverLogic { case (groupId, teacherId) =>
    groupService.putApiAdminGroupsGroupIdTeacherTeacherIdHandle(groupId, teacherId).value
  }

  def deleteApiAdminGroupsGroupIdTeacherTeacherIdLogic = deleteApiAdminGroupsGroupIdTeacherTeacherId.serverLogic { case (groupId, teacherId) =>
    groupService.deleteApiAdminGroupsGroupIdTeacherTeacherIdHandle(groupId, teacherId).value
  }

  def putApiAdminGroupsGroupIdKpiKpiIdLogic = putApiAdminGroupsGroupIdKpiKpiId.serverLogic { case (groupId, kpiId) =>
    groupService.putApiAdminGroupsGroupIdKpiKpiIdHandle(groupId, kpiId).value
  }

  def deleteApiAdminGroupsGroupIdKpiKpiIdLogic = deleteApiAdminGroupsGroupIdKpiKpiId.serverLogic { case (groupId, kpiId) =>
    groupService.deleteApiAdminGroupsGroupIdKpiKpiIdHandle(groupId, kpiId).value
  }

  def createTopicEndpointLogic = createTopicEndpoint.serverLogic { params =>
    service.createTopicEndpointHandle(params).value
  }

  def getAllTopicsEndpointLogic = getAllTopicsEndpoint.serverLogic { params =>
    service.getAllTopicsEndpointHandle(params).value
  }

  def deleteTopicEndpointLogic = deleteTopicEndpoint.serverLogic { params =>
    service.deleteTopicEndpointHandle(params).value
  }

  def editTopicNameEndpointLogic = editTopicNameEndpoint.serverLogic { params =>
    service.editTopicNameEndpointHandle(params).value
  }

  def createKpiEndpointLogic = createKpiEndpoint.serverLogic { params =>
    service.createKpiEndpointHandle(params).value
  }

  def getAllKpiEndpointLogic = getAllKpiEndpoint.serverLogic { params =>
    service.getAllKpiEndpointHandle(params).value
  }

  def editKpiEndpointLogic = editKpiEndpoint.serverLogic { params =>
    service.editKpiEndpointHandle(params).value
  }

  def deleteKpiEndpointLogic = deleteKpiEndpoint.serverLogic { params =>
    service.deleteKpiEndpointHandle(params).value
  }

  def createTeachersEndpointLogic = createTeachersEndpoint.serverLogic { params =>
    service.createTeachersEndpointHandle(params).value
  }

  def getTeachersEndpointLogic = getTeachersEndpoint.serverLogic { params =>
    service.getTeachersEndpointHandle(params).value
  }

  def editTeachersEndpointLogic = editTeachersEndpoint.serverLogic { params =>
    service.editTeachersEndpointHandle(params).value
  }

  def deleteTeachersEndpointLogic = deleteTeachersEndpoint.serverLogic { params =>
    service.deleteTeachersEndpointHandle(params).value
  }

  def createReviewersEndpointLogic = createReviewersEndpoint.serverLogic { params =>
    service.createReviewersEndpointHandle(params).value
  }

  def getAllReviewersEndpointLogic = getAllReviewersEndpoint.serverLogic { params =>
    service.getAllReviewersEndpointHandle(params).value
  }

  def editReviewersEndpointLogic = editReviewersEndpoint.serverLogic { params =>
    service.editReviewersEndpointHandle(params).value
  }

  def deleteReviewersEndpointLogic = deleteReviewersEndpoint.serverLogic { params =>
    service.deleteReviewersEndpointHandle(params).value
  }

  def loginEndpointLogic = loginEndpoint.serverLogic { params =>
    authenticator.authenticate(params.login, params.password).map(BearerResponseDto.apply).toRight(unauthorized).value
  }

  def getKpiDataForTeacherLogic = getKpiDataForTeacher.serverLogic { params =>
    service.getKpiDataForTeacherHandle(params).value
  }

  def getKpisByTeacherForReviewerLogic = getKpisByTeacherForReviewer.serverLogic { params =>
    service.getKpisByTeacherForReviewerHandle(params).value
  }

  def estimateTeacherEndpointLogic = estimateTeacherEndpoint.serverLogic { params =>
    service.estimateTeacherEndpointHandle(params).value
  }

  def fillKpiEndpointLogic = fillKpiEndpoint.serverLogic { params =>
    service.fillKpiEndpointHandle(params).value
  }

}
