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
    service.createGroupEndpointHandle(params)
  }

  def getAllGroupsEndpointLogic = getAllGroupsEndpoint.serverLogic { params =>
    service.getAllGroupsEndpointHandle(params)
  }

  def deleteGroupEndpointLogic = deleteGroupEndpoint.serverLogic { params =>
    service.deleteGroupEndpointHandle(params)
  }

  def editGroupEndpointLogic = editGroupEndpoint.serverLogic { params =>
    service.editGroupEndpointHandle(params)
  }

  def putApiAdminGroupsGroupIdTeacherTeacherIdLogic = putApiAdminGroupsGroupIdTeacherTeacherId.serverLogic { params =>
    service.putApiAdminGroupsGroupIdTeacherTeacherIdHandle(params)
  }

  def deleteApiAdminGroupsGroupIdTeacherTeacherIdLogic = deleteApiAdminGroupsGroupIdTeacherTeacherId.serverLogic { params =>
    service.deleteApiAdminGroupsGroupIdTeacherTeacherIdHandle(params)
  }

  def putApiAdminGroupsGroupIdKpiKpiIdLogic = putApiAdminGroupsGroupIdKpiKpiId.serverLogic { params =>
    service.putApiAdminGroupsGroupIdKpiKpiIdHandle(params)
  }

  def deleteApiAdminGroupsGroupIdKpiKpiIdLogic = deleteApiAdminGroupsGroupIdKpiKpiId.serverLogic { params =>
    service.deleteApiAdminGroupsGroupIdKpiKpiIdHandle(params)
  }

  def createTopicEndpointLogic = createTopicEndpoint.serverLogic { params =>
    service.createTopicEndpointHandle(params)
  }

  def getAllTopicsEndpointLogic = getAllTopicsEndpoint.serverLogic { params =>
    service.getAllTopicsEndpointHandle(params)
  }

  def deleteTopicEndpointLogic = deleteTopicEndpoint.serverLogic { params =>
    service.deleteTopicEndpointHandle(params)
  }

  def editTopicNameEndpointLogic = editTopicNameEndpoint.serverLogic { params =>
    service.editTopicNameEndpointHandle(params)
  }

  def createKpiEndpointLogic = createKpiEndpoint.serverLogic { params =>
    service.createKpiEndpointHandle(params)
  }

  def getAllKpiEndpointLogic = getAllKpiEndpoint.serverLogic { params =>
    service.getAllKpiEndpointHandle(params)
  }

  def editKpiEndpointLogic = editKpiEndpoint.serverLogic { params =>
    service.editKpiEndpointHandle(params)
  }

  def deleteKpiEndpointLogic = deleteKpiEndpoint.serverLogic { params =>
    service.deleteKpiEndpointHandle(params)
  }

  def createTeachersEndpointLogic = createTeachersEndpoint.serverLogic { params =>
    service.createTeachersEndpointHandle(params)
  }

  def getTeachersEndpointLogic = getTeachersEndpoint.serverLogic { params =>
    service.getTeachersEndpointHandle(params)
  }

  def editTeachersEndpointLogic = editTeachersEndpoint.serverLogic { params =>
    service.editTeachersEndpointHandle(params)
  }

  def deleteTeachersEndpointLogic = deleteTeachersEndpoint.serverLogic { params =>
    service.deleteTeachersEndpointHandle(params)
  }

  def createReviewersEndpointLogic = createReviewersEndpoint.serverLogic { params =>
    service.createReviewersEndpointHandle(params)
  }

  def getAllReviewersEndpointLogic = getAllReviewersEndpoint.serverLogic { params =>
    service.getAllReviewersEndpointHandle(params)
  }

  def editReviewersEndpointLogic = editReviewersEndpoint.serverLogic { params =>
    service.editReviewersEndpointHandle(params)
  }

  def deleteReviewersEndpointLogic = deleteReviewersEndpoint.serverLogic { params =>
    service.deleteReviewersEndpointHandle(params)
  }

  def loginEndpointLogic = loginEndpoint.serverLogic { params =>
    service.loginEndpointHandle(params)
  }

  def getKpiDataForTeacherLogic = getKpiDataForTeacher.serverLogic { params =>
    service.getKpiDataForTeacherHandle(params)
  }

  def getKpisByTeacherForReviewerLogic = getKpisByTeacherForReviewer.serverLogic { params =>
    service.getKpisByTeacherForReviewerHandle(params)
  }

  def estimateTeacherEndpointLogic = estimateTeacherEndpoint.serverLogic { params =>
    service.estimateTeacherEndpointHandle(params)
  }

  def fillKpiEndpointLogic = fillKpiEndpoint.serverLogic { params =>
    service.fillKpiEndpointHandle(params)
  }

}
