package org.sibadi.auditing.api.routes

import cats.effect.Async
import org.sibadi.auditing.api.endpoints.AdminAPI._
import org.sibadi.auditing.api.endpoints.FullApi._
import org.sibadi.auditing.service.AllService

class AllRouter[F[_]: Async](
  service: AllService[F]
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
    service.createGroupEndpointHandle(params).value
  }

  def getAllGroupsEndpointLogic = getAllGroupsEndpoint.serverLogic { params =>
    service.getAllGroupsEndpointHandle(params).value
  }

  def deleteGroupEndpointLogic = deleteGroupEndpoint.serverLogic { params =>
    service.deleteGroupEndpointHandle(params).value
  }

  def editGroupEndpointLogic = editGroupEndpoint.serverLogic { params =>
    service.editGroupEndpointHandle(params).value
  }

  def putApiAdminGroupsGroupIdTeacherTeacherIdLogic = putApiAdminGroupsGroupIdTeacherTeacherId.serverLogic { params =>
    service.putApiAdminGroupsGroupIdTeacherTeacherIdHandle(params).value
  }

  def deleteApiAdminGroupsGroupIdTeacherTeacherIdLogic = deleteApiAdminGroupsGroupIdTeacherTeacherId.serverLogic { params =>
    service.deleteApiAdminGroupsGroupIdTeacherTeacherIdHandle(params).value
  }

  def putApiAdminGroupsGroupIdKpiKpiIdLogic = putApiAdminGroupsGroupIdKpiKpiId.serverLogic { params =>
    service.putApiAdminGroupsGroupIdKpiKpiIdHandle(params).value
  }

  def deleteApiAdminGroupsGroupIdKpiKpiIdLogic = deleteApiAdminGroupsGroupIdKpiKpiId.serverLogic { params =>
    service.deleteApiAdminGroupsGroupIdKpiKpiIdHandle(params).value
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
    service.loginEndpointHandle(params).value
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
