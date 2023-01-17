package org.sibadi.auditing.api.routes

import cats.Monad
import cats.effect.Sync
import cats.syntax.applicative._
import cats.syntax.either._
import org.sibadi.auditing.api.endpoints.TopicsAPI._
import org.sibadi.auditing.api.model.{ApiError, TopicItemResponseDto, toApiError}
import org.sibadi.auditing.service._
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.{Logger, SelfAwareStructuredLogger}

class TopicsRouter[F[_]: Sync](
  authenticator: Authenticator[F],
  estimateService: EstimateService[F],
  groupService: GroupService[F],
  kpiService: KpiService[F],
  reviewerService: ReviewerService[F],
  teacherService: TeacherService[F],
  topicService: TopicService[F]
) {

  private implicit def logger: Logger[F] = Slf4jLogger.getLogger

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
          .leftSemiflatMap(toApiError[F])
          .value
      }

  private def adminGetTopic =
    getApiAdminTopics
      .serverSecurityLogic { token =>
        authenticator.atLeastReviewer(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType => body =>
        topicService.getAllTopics
          .leftSemiflatMap(toApiError[F])
          .map(_.map(topic => TopicItemResponseDto(topic.id, topic.title, List.empty)))
          .value
      }

  private def adminDeleteTopic =
    deleteApiAdminTopics
      .serverSecurityLogic { token =>
        authenticator.isAdmin(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType => body =>
        ApiError.InternalError("Not implemented").cast.asLeft[Unit].pure[F]
      }

  private def adminEditTopicsById =
    putApiAdminTopicsTopicId
      .serverSecurityLogic { token =>
        authenticator.isAdmin(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType => body =>
        topicService
          .updateTopic(body._1, body._2.name)
          .leftSemiflatMap(toApiError[F])
          .value
      }

}
