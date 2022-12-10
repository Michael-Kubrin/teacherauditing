package org.sibadi.auditing

import cats.effect._
import doobie.util.transactor.Transactor
import org.http4s.HttpRoutes
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.{Router, Server}
import org.sibadi.auditing.api.routes._
import org.sibadi.auditing.configs.{AppConfig, DatabaseConfig, ServerConfig}
import org.sibadi.auditing.db._
import org.sibadi.auditing.service._
import org.sibadi.auditing.util.{Filer, TokenGenerator}
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

object Main extends IOApp.Simple {

  implicit def logger[F[_]: Sync]: Logger[F] = Slf4jLogger.getLogger

  override def run: IO[Unit] =
    main[IO].use { _ =>
      IO.never
    }.void

  private def main[F[_]: Async]: Resource[F, Server] =
    for {
      cfg        <- Resource.eval(AppConfig.read)
      _          <- Resource.eval(Logger[F].trace(AppConfig.show.show(cfg)))
      _          <- Resource.eval(Migrations(cfg.database).migrate())
      transactor <- createTransactor(cfg.database)
      // DAOs
      teacher             = new TeacherDAO(transactor)
      teacherCredentials  = new TeacherCredentialsDAO(transactor)
      reviewer            = new ReviewerDAO(transactor)
      reviewerCredentials = new ReviewerCredentialsDAO(transactor)
      group               = new GroupDAO(transactor)
      kpi                 = new KpiDAO(transactor)
      topic               = new TopicDAO(transactor)
      topicKpi            = new TopicKpiDAO(transactor)
      kpiGroup            = new KpiGroupDAO(transactor)
      teacherGroup        = new TeacherGroupDAO(transactor)
      estimate            = new EstimateDAO(transactor)
      estimateFiles       = new EstimateFilesDAO(transactor)
      // Utils
      authenticator <- Authenticator[F](teacherCredentials, reviewerCredentials, cfg.admin)
      filer          = new Filer[F]
      tokenGenerator = TokenGenerator()
      // Service
      estimateService <- EstimateService[F](estimate, teacherGroup, estimateFiles, filer)
      groupService    <- GroupService[F](group)
      kpiService      <- KpiService[F](kpi, kpiGroup)
      reviewerService <- ReviewerService[F](tokenGenerator, reviewer, reviewerCredentials)
      teacherService  <- TeacherService[F](tokenGenerator, teacher, teacherCredentials, teacherGroup)
      topicService    <- TopicService[F](topic, kpi, topicKpi)
      // Router
      groupsRouter   = new GroupsRouter[F](authenticator, estimateService, groupService, kpiService, reviewerService, teacherService, topicService)
      kpiGroupRouter = new KpiGroupRouter[F](authenticator, estimateService, groupService, kpiService, reviewerService, teacherService, topicService)
      kpiRouter      = new KpiRouter[F](authenticator, estimateService, groupService, kpiService, reviewerService, teacherService, topicService)
      kpiTeacherRouter = new KpiTeacherRouter[F](
        authenticator,
        estimateService,
        groupService,
        kpiService,
        reviewerService,
        teacherService,
        topicService
      )
      publicRouter = new PublicRouter[F](authenticator, estimateService, groupService, kpiService, reviewerService, teacherService, topicService)
      reviewerActionsRouter = new ReviewerActionsRouter[F](
        authenticator,
        estimateService,
        groupService,
        kpiService,
        reviewerService,
        teacherService,
        topicService
      )
      reviewersRouter = new ReviewersRouter[F](
        authenticator,
        estimateService,
        groupService,
        kpiService,
        reviewerService,
        teacherService,
        topicService
      )
      teacherActionsRouter = new TeacherActionsRouter[F](
        authenticator,
        estimateService,
        groupService,
        kpiService,
        reviewerService,
        teacherService,
        topicService
      )
      teacherRouter = new TeachersRouter[F](authenticator, estimateService, groupService, kpiService, reviewerService, teacherService, topicService)
      topicsRouter  = new TopicsRouter[F](authenticator, estimateService, groupService, kpiService, reviewerService, teacherService, topicService)
      router <- AppRouter[F](
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
      server <- server[F](cfg.server, router.httpRoutes)
    } yield server

  private def createTransactor[F[_]: Async](cfg: DatabaseConfig): Resource[F, Transactor[F]] =
    Resource.eval {
      Sync[F].blocking {
        Transactor.fromDriverManager[F](
          "org.postgresql.Driver",
          cfg.jdbcUrl,  // connect URL (driver-specific)
          cfg.username, // user
          cfg.password  // password
        )
      }
    }

  private def server[F[_]: Async](cfg: ServerConfig, routes: HttpRoutes[F]): Resource[F, Server] =
    BlazeServerBuilder[F]
      .bindHttp(cfg.port, cfg.host)
      .withHttpApp(Router("/" -> routes).orNotFound)
      .resource
}
