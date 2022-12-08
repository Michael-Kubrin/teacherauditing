package org.sibadi.auditing.api.routes

import cats.Monad
import org.sibadi.auditing.api.endpoints.TeacherActionsAPI._
import org.sibadi.auditing.api.endpoints.TopicsAPI._
import org.sibadi.auditing.api.model.{toApiError, ApiError}
import org.sibadi.auditing.service.{Authenticator, TopicService}

class TopicRouter[F[_]: Monad](
  authenticator: Authenticator[F],
  topicService: TopicService[F]
) {

  def routes = List(adminCreateTopic, adminGetTopic, adminDeleteTopic, adminEditTopicsById, publicGetTopics)

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
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType => body =>
        topicService.getAllTopics
          .leftMap(toApiError)
          .value
      }

  private def adminDeleteTopic =
    deleteApiAdminTopics
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
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

  private def adminEditTopicsById =
    putApiAdminTopicsTopicId
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType => body =>
        for {
          topicName <- body._2.name
          topicId   <- body._1
          x         <- topicService.updateTopic(topicId, topicName)
        } yield x

//        val topicName = body._2.name
//        val topicId   = body._1 // TODO: not sure
//        topicService
//          .updateTopic(topicId, topicName)
//          .leftMap(toApiError)
//          .value
      }

  private def publicGetTopics =
    getApiPublicTopics
      .serverSecurityLogic { token =>
        authenticator.authenticate(token).toRight(ApiError.Unauthorized("Unauthorized").cast).value
      }
      .serverLogic { userType => body =>
        topicService
          .getTopic(groupId = ???)
          .leftMap(toApiError)
          .value
      }

}
