package org.sibadi.auditing

import cats.effect._
import org.http4s.server.Server
import org.sibadi.auditing.api.{AllHttpRoutes, AppRouter, HttpServer}
import org.sibadi.auditing.configs.AppConfig
import org.sibadi.auditing.db._
import org.sibadi.auditing.service.refucktor._
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
      cfg <- Resource.eval(AppConfig.read)
      _   <- Resource.eval(Logger[F].trace(AppConfig.show.show(cfg)))
      db  <- DbComponent(cfg)
      service   = ServiceComponent(db, cfg.admin)
      allRouter = AllHttpRoutes[F](service)
      router    = new AppRouter[F](allRouter)
      server <- HttpServer.server[F](cfg.server, router.httpRoutes)
      _      <- Resource.eval(Logger[F].info(s"Server started at ${cfg.server.host}:${cfg.server.port}"))
    } yield server

}
