package org.sibadi.auditing.api.routes

import cats.Monad
import cats.syntax.applicative._
import cats.syntax.either._
import org.sibadi.auditing.api.endpoints.TeacherActionsAPI._
import org.sibadi.auditing.api.model._
import org.sibadi.auditing.service._

class TeacherActionsRouter[F[_]: Monad](
  authenticator: Authenticator[F],
  estimateService: EstimateService[F],
  groupService: GroupService[F],
  kpiService: KpiService[F],
  reviewerService: ReviewerService[F],
  teacherService: TeacherService[F],
  topicService: TopicService[F]
) {

  def routes = List(publicEstimate, publicUploadFile, publicGetKpi, publicGetTopics)

  private def publicEstimate =
    postApiPublicTopicsTopicIdKpiKpiIdEstimate
      .serverSecurityLogic { token =>
        authenticator.isTeacher(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType => body =>
        estimateService
          .createEstimate(topicId = body._1, kpiId = body._2, teacherId = userType.id, score = body._3.score)
          .leftMap(toApiError)
          .value
      }

  def publicUploadFile =
    postApiPublicTopicsTopicIdKpiKpiIdFiles
      .serverSecurityLogic { token =>
        authenticator.isTeacher(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType => body =>
        estimateService
          .createEstimateFiles(topicId = body._1, kpiId = body._2, userType.id, body._3)
          .leftMap(toApiError)
          .value
      }

  //TODO: How to put all values of GetPublicKpiResponse?
  private def publicGetKpi =
    getApiPublicTopicsTopicIdKpi
      .serverSecurityLogic { token =>
        authenticator.isTeacher(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType => body =>
//        topicService.getTopicKpiByKpiId(body)
//          .leftMap(toApiError)
//          .map(_.map(x => GetPublicKpiResponse()))
        ApiError.InternalError("Not implemented").cast.asLeft[GetPublicKpiResponse].pure[F]
      }

  private def publicGetTopics =
    getApiPublicTopics
      .serverSecurityLogic { token =>
        authenticator.atLeastTeacher(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType => body =>
        topicService.getAllTopics
          .leftMap(toApiError)
          .map(_.map(topic => TopicItemResponseDto(topic.id, topic.title, List.empty))) // TODO
          .value
      }

}
