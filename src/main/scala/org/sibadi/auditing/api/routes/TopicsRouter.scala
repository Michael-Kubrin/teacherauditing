package org.sibadi.auditing.api.routes

import cats.Monad
import cats.syntax.applicative._
import cats.syntax.either._
import org.sibadi.auditing.api.endpoints.TeacherActionsAPI._
import org.sibadi.auditing.api.endpoints.TopicsAPI._
import org.sibadi.auditing.api.model.{ApiError, TopicItemResponseDto, toApiError}
import org.sibadi.auditing.service._

class TopicsRouter[F[_]: Monad](
  authenticator: Authenticator[F],
  estimateService: EstimateService[F],
  groupService: GroupService[F],
  kpiService: KpiService[F],
  reviewerService: ReviewerService[F],
  teacherService: TeacherService[F],
  topicService: TopicService[F]
) {

  def routes = List(adminCreateTopic, adminGetTopic, adminDeleteTopic, adminEditTopicsById)

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

  private def adminGetTopic =
    getApiAdminTopics
      .serverSecurityLogic { token =>
        authenticator.atLeastReviewer(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType => body =>
        topicService.getAllTopics
          .leftMap(toApiError)
          .map(_.map(topic => TopicItemResponseDto(topic.id, topic.title, List.empty)))
          .value
      }

  private def adminDeleteTopic =
    deleteApiAdminTopics
      .serverSecurityLogic { token =>
        authenticator.isAdmin(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType =>
        body =>
          ApiError.InternalError("Not implemented").cast.asLeft[Unit].pure[F]
      }

  private def adminEditTopicsById =
    putApiAdminTopicsTopicId
      .serverSecurityLogic { token =>
        authenticator.isAdmin(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType => body =>
        topicService.updateTopic(body._1, body._2.name)
          .leftMap(toApiError)
          .value
      }

}
