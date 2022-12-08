package org.sibadi.auditing.api.routes

import cats.effect.{Async, Resource}
import org.http4s.HttpRoutes
import org.sibadi.auditing.api.endpoints.AppEndpoints
import org.sibadi.auditing.service._
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter

class AppRouter[F[_]: Async](
  authenticator: Authenticator[F],
  service: Service[F],
  teacherRouter: TeacherRouter[F],
  reviewersRouter: ReviewersRouter[F],
  topicRouter: TopicRouter[F],
  estimateRouter: EstimateRouter[F],
  groupsRouter: GroupsRouter[F]
) {

  private val docRoutes: List[ServerEndpoint[Any, F]] = SwaggerInterpreter().fromEndpoints[F](AppEndpoints.allEndpoints, "aboba", "1.0.0")

  def httpRoutes: HttpRoutes[F] = Http4sServerInterpreter[F]().toRoutes(allRoutes ++ docRoutes)

  private def allRoutes: List[ServerEndpoint[Any, F]] = List(
    teacherRouter.routes ++
      reviewersRouter.routes ++
      groupsRouter.routes ++
      estimateRouter.routes ++
      topicRouter.routes ++
      groupsRouter.routes
  )

}

object AppRouter {
  def apply[F[_]: Async](
    authenticator: Authenticator[F],
    service: Service[F],
    teacherRouter: TeacherRouter[F],
    reviewersRouter: ReviewersRouter[F],
    topicRouter: TopicRouter[F],
    estimateRouter: EstimateRouter[F],
    groupsRouter: GroupsRouter[F]
  ): Resource[F, AppRouter[F]] =
    Resource.pure(new AppRouter(authenticator, service, teacherRouter, reviewersRouter, topicRouter, estimateRouter, groupsRouter))
}
