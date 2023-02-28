package org.sibadi.auditing

import cats.effect._
import doobie.util.transactor.Transactor
import org.http4s.HttpRoutes
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.middleware.CORS
import org.http4s.server.{Router, Server}
import org.sibadi.auditing.api.routes._
import org.sibadi.auditing.configs.{AppConfig, DatabaseConfig, ServerConfig}
import org.sibadi.auditing.db._
import org.sibadi.auditing.service.refucktor._
import org.sibadi.auditing.service.{AllService, Authenticator}
import org.sibadi.auditing.util.{Filer, HashGenerator, TokenGenerator}
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
      filer          = new Filer[F]
      tokenGenerator = TokenGenerator()
      hashGenerator  = new HashGenerator()
      authenticator <- Authenticator[F](teacherCredentials, reviewerCredentials, cfg.admin, tokenGenerator, hashGenerator)
      // Service
      allService   = new AllService[F](transactor)
      groupService = new GroupService[F](transactor)
      allRouter    = new AllRouter[F](allService, groupService, authenticator)
      router       = new AppRouter[F](allRouter)

      cors = CORS.policy.withAllowOriginAll
        .withAllowCredentials(false)
        .apply(router.httpRoutes)

      server <- server[F](cfg.server, cors)
      _      <- Resource.eval(Logger[F].info(s"Server started at ${cfg.server.host}:${cfg.server.port}"))
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
