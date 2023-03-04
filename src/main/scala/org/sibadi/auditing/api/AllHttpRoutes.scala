package org.sibadi.auditing.api

import cats.effect.Async
import org.sibadi.auditing.api.ApiErrors.unauthorized
import org.sibadi.auditing.api.endpoints.AdminAPI._
import org.sibadi.auditing.api.endpoints.FullApi._
import org.sibadi.auditing.api.endpoints.model.BearerResponseDto
import org.sibadi.auditing.service.refucktor._
import org.typelevel.log4cats.Logger
import sttp.tapir.server.ServerEndpoint

class AllHttpRoutes[F[_]: Async: Logger](
  service: ServiceComponent[F]
) {

  def createGroupEndpointLogic = createGroupEndpoint.serverLogic { params =>
    service.group.createGroupEndpointHandle(params).value
  }

  def getAllGroupsEndpointLogic = getAllGroupsEndpoint.serverLogic { _ =>
    service.group.getAllGroupsEndpointHandle.value
  }

  def deleteGroupEndpointLogic = deleteGroupEndpoint.serverLogic { groupId =>
    service.group.deleteGroupEndpointHandle(groupId).value
  }

  def editGroupEndpointLogic = editGroupEndpoint.serverLogic { case (groupId, body) =>
    service.group.editGroupEndpointHandle(groupId, body).value
  }

  def putApiAdminGroupsGroupIdTeacherTeacherIdLogic = putApiAdminGroupsGroupIdTeacherTeacherId.serverLogic { case (groupId, teacherId) =>
    service.group.putApiAdminGroupsGroupIdTeacherTeacherIdHandle(groupId, teacherId).value
  }

  def deleteApiAdminGroupsGroupIdTeacherTeacherIdLogic = deleteApiAdminGroupsGroupIdTeacherTeacherId.serverLogic { case (groupId, teacherId) =>
    service.group.deleteApiAdminGroupsGroupIdTeacherTeacherIdHandle(groupId, teacherId).value
  }

  def putApiAdminGroupsGroupIdKpiKpiIdLogic = putApiAdminGroupsGroupIdKpiKpiId.serverLogic { case (groupId, kpiId) =>
    service.group.putApiAdminGroupsGroupIdKpiKpiIdHandle(groupId, kpiId).value
  }

  def deleteApiAdminGroupsGroupIdKpiKpiIdLogic = deleteApiAdminGroupsGroupIdKpiKpiId.serverLogic { case (groupId, kpiId) =>
    service.group.deleteApiAdminGroupsGroupIdKpiKpiIdHandle(groupId, kpiId).value
  }

  def createTopicEndpointLogic = createTopicEndpoint.serverLogic { params =>
    service.topic.create(params).value
  }

  def getAllTopicsEndpointLogic = getAllTopicsEndpoint.serverLogic { params =>
    service.topic.getAll.value
  }

  def deleteTopicEndpointLogic = deleteTopicEndpoint.serverLogic { topicId =>
    service.topic.delete(topicId).value
  }

  def editTopicNameEndpointLogic = editTopicNameEndpoint.serverLogic { params =>
    service.topic.edit(params._1, params._2).value
  }

  def createKpiEndpointLogic = createKpiEndpoint.serverLogic { params =>
    service.kpi.create(params._1, params._2).value
  }

  def getAllKpiEndpointLogic = getAllKpiEndpoint.serverLogic { params =>
    service.kpi.getAll(params).value
  }

  def editKpiEndpointLogic = editKpiEndpoint.serverLogic { params =>
    service.kpi.edit(params._2, params._3).value
  }

  def deleteKpiEndpointLogic = deleteKpiEndpoint.serverLogic { params =>
    service.kpi.delete(params._1, params._2).value
  }

  def createTeachersEndpointLogic = createTeachersEndpoint.serverLogic { params =>
    service.register
      .registerTeacher(
        rawFirstName = params.firstName,
        rawLastName = params.lastName,
        rawMiddleName = params.middleName,
        rawLogin = params.login
      )
      .value
  }

  def getTeachersEndpointLogic = getTeachersEndpoint.serverLogic { _ =>
    service.teacher.getAll.value
  }

  def editTeachersEndpointLogic = editTeachersEndpoint.serverLogic { params =>
    service.teacher.edit(params._1, params._2).value
  }

  def deleteTeachersEndpointLogic = deleteTeachersEndpoint.serverLogic { params =>
    service.teacher.delete(params).value
  }

  def createReviewersEndpointLogic = createReviewersEndpoint.serverLogic { params =>
    service.register
      .registerReviewer(
        rawFirstName = params.firstName,
        rawLastName = params.lastName,
        rawMiddleName = params.middleName,
        rawLogin = params.login
      )
      .value
  }

  def getAllReviewersEndpointLogic = getAllReviewersEndpoint.serverLogic { _ =>
    service.reviewer.getAll.value
  }

  def editReviewersEndpointLogic = editReviewersEndpoint.serverLogic { params =>
    service.reviewer.edit(params._1, params._2).value
  }

  def deleteReviewersEndpointLogic = deleteReviewersEndpoint.serverLogic { params =>
    service.reviewer.delete(params).value
  }

  def loginEndpointLogic = loginEndpoint.serverLogic { params =>
    service.auth.authenticate(params.login, params.password).map(BearerResponseDto.apply).toRight(unauthorized).value
  }

  def getKpiDataForTeacherLogic = getKpiDataForTeacher.serverLogic { params =>
    service.all.getKpiDataForTeacherHandle(params).value
  }

  def getKpisByTeacherForReviewerLogic = getKpisByTeacherForReviewer.serverLogic { params =>
    service.all.getKpisByTeacherForReviewerHandle(params).value
  }

  def estimateTeacherEndpointLogic = estimateTeacherEndpoint.serverLogic { params =>
    service.all.estimateTeacherEndpointHandle(params).value
  }

  def fillKpiEndpointLogic = fillKpiEndpoint.serverLogic { params =>
    service.all.fillKpiEndpointHandle(params).value
  }

}

object AllHttpRoutes {

  def apply[F[_]: Async: Logger](
    service: ServiceComponent[F]
  ): List[ServerEndpoint.Full[_, _, _, _, _, Any, F]] = {
    val routes = new AllHttpRoutes(service)
    List(
      routes.createGroupEndpointLogic,
      routes.getAllGroupsEndpointLogic,
      routes.deleteGroupEndpointLogic,
      routes.editGroupEndpointLogic,
      routes.putApiAdminGroupsGroupIdTeacherTeacherIdLogic,
      routes.deleteApiAdminGroupsGroupIdTeacherTeacherIdLogic,
      routes.putApiAdminGroupsGroupIdKpiKpiIdLogic,
      routes.deleteApiAdminGroupsGroupIdKpiKpiIdLogic,
      routes.createTopicEndpointLogic,
      routes.getAllTopicsEndpointLogic,
      routes.deleteTopicEndpointLogic,
      routes.editTopicNameEndpointLogic,
      routes.createKpiEndpointLogic,
      routes.getAllKpiEndpointLogic,
      routes.editKpiEndpointLogic,
      routes.deleteKpiEndpointLogic,
      routes.createTeachersEndpointLogic,
      routes.getTeachersEndpointLogic,
      routes.editTeachersEndpointLogic,
      routes.deleteTeachersEndpointLogic,
      routes.createReviewersEndpointLogic,
      routes.getAllReviewersEndpointLogic,
      routes.editReviewersEndpointLogic,
      routes.deleteReviewersEndpointLogic,
      routes.loginEndpointLogic,
      routes.getKpiDataForTeacherLogic,
      routes.getKpisByTeacherForReviewerLogic,
      routes.estimateTeacherEndpointLogic,
      routes.fillKpiEndpointLogic
    )
  }

}
