package org.sibadi.auditing.api.routes

import cats.effect.Async
import org.sibadi.auditing.api.endpoints.AdminAPI._
import org.sibadi.auditing.api.endpoints.FullApi._
import org.sibadi.auditing.api.endpoints.model.BearerResponseDto
import org.sibadi.auditing.api.ApiErrors.unauthorized
import org.sibadi.auditing.service.refucktor._
import org.sibadi.auditing.service.{AllService, Authenticator}
import org.typelevel.log4cats.Logger

class AllRouter[F[_]: Async: Logger](
  service: AllService[F],
  groupService: GroupService[F],
  authenticator: Authenticator[F],
  registerService: RegisterService[F],
  teacherService: TeacherService[F],
  reviewerService: ReviewerService[F],
  kpiService: KpiService[F],
  topicService: TopicService[F]
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
    topicService.create(params).value
  }

  def getAllTopicsEndpointLogic = getAllTopicsEndpoint.serverLogic { params =>
    topicService.getAll.value
  }

  def deleteTopicEndpointLogic = deleteTopicEndpoint.serverLogic { topicId =>
    topicService.delete(topicId).value
  }

  def editTopicNameEndpointLogic = editTopicNameEndpoint.serverLogic { params =>
    topicService.edit(params._1, params._2).value
  }

  def createKpiEndpointLogic = createKpiEndpoint.serverLogic { params =>
    kpiService.create(params._1, params._2).value
  }

  def getAllKpiEndpointLogic = getAllKpiEndpoint.serverLogic { params =>
    kpiService.getAll(params).value
  }

  def editKpiEndpointLogic = editKpiEndpoint.serverLogic { params =>
    kpiService.edit(params._2, params._3).value
  }

  def deleteKpiEndpointLogic = deleteKpiEndpoint.serverLogic { params =>
    kpiService.delete(params._1, params._2).value
  }

  def createTeachersEndpointLogic = createTeachersEndpoint.serverLogic { params =>
    registerService
      .registerTeacher(
        rawFirstName = params.name,
        rawLastName = params.surName,
        rawMiddleName = params.middleName,
        rawLogin = params.login
      )
      .value
  }

  def getTeachersEndpointLogic = getTeachersEndpoint.serverLogic { _ =>
    teacherService.getAll.value
  }

  def editTeachersEndpointLogic = editTeachersEndpoint.serverLogic { params =>
    teacherService.edit(params._1, params._2).value
  }

  def deleteTeachersEndpointLogic = deleteTeachersEndpoint.serverLogic { params =>
    teacherService.delete(params).value
  }

  def createReviewersEndpointLogic = createReviewersEndpoint.serverLogic { params =>
    registerService
      .registerReviewer(
        rawFirstName = params.name,
        rawLastName = params.surName,
        rawMiddleName = params.middleName,
        rawLogin = params.login
      )
      .value
  }

  def getAllReviewersEndpointLogic = getAllReviewersEndpoint.serverLogic { _ =>
    reviewerService.getAll.value
  }

  def editReviewersEndpointLogic = editReviewersEndpoint.serverLogic { params =>
    reviewerService.edit(params._1, params._2).value
  }

  def deleteReviewersEndpointLogic = deleteReviewersEndpoint.serverLogic { params =>
    reviewerService.delete(params).value
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
