package org.sibadi.auditing.api.routes

import cats.effect.{Async, Resource}
import org.http4s.HttpRoutes
import org.sibadi.auditing.api.endpoints.AppEndpoints
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter

class AppRouter[F[_]: Async](
  groupsRouter: GroupsRouter[F],
  kpiGroupRouter: KpiGroupRouter[F],
  kpiRouter: KpiRouter[F],
  kpiTeacherRouter: KpiTeacherRouter[F],
  publicRouter: PublicRouter[F],
  reviewerActionsRouter: ReviewerActionsRouter[F],
  reviewersRouter: ReviewersRouter[F],
  teacherActionsRouter: TeacherActionsRouter[F],
  teacherRouter: TeachersRouter[F],
  topicsRouter: TopicsRouter[F]
) {

  private val docRoutes: List[ServerEndpoint[Any, F]] = SwaggerInterpreter().fromEndpoints[F](AppEndpoints.allEndpoints, "aboba", "1.0.0")

  def httpRoutes: HttpRoutes[F] = Http4sServerInterpreter[F]().toRoutes(allRoutes ++ docRoutes)

  private def allRoutes: List[ServerEndpoint[Any, F]] =
    groupsRouter.routes ++
      kpiGroupRouter.routes ++
      kpiRouter.routes ++
      kpiTeacherRouter.routes ++
      publicRouter.routes ++
      reviewerActionsRouter.routes ++
      reviewersRouter.routes ++
      teacherActionsRouter.routes ++
      teacherRouter.routes ++
      topicsRouter.routes

}

object AppRouter {
  def apply[F[_]: Async](
    groupsRouter: GroupsRouter[F],
    kpiGroupRouter: KpiGroupRouter[F],
    kpiRouter: KpiRouter[F],
    kpiTeacherRouter: KpiTeacherRouter[F],
    publicRouter: PublicRouter[F],
    reviewerActionsRouter: ReviewerActionsRouter[F],
    reviewersRouter: ReviewersRouter[F],
    teacherActionsRouter: TeacherActionsRouter[F],
    teacherRouter: TeachersRouter[F],
    topicsRouter: TopicsRouter[F]
  ): Resource[F, AppRouter[F]] =
    Resource.pure(
      new AppRouter(
        groupsRouter,
        kpiGroupRouter,
        kpiRouter,
        kpiTeacherRouter,
        publicRouter,
        reviewerActionsRouter,
        reviewersRouter,
        teacherActionsRouter,
        teacherRouter,
        topicsRouter
      )
    )
}
