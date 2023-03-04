package org.sibadi.auditing

import cats.effect._
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.{Router, Server}
import org.sibadi.auditing.api.AppRouter
import org.sibadi.auditing.configs.AppConfig
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import sttp.tapir.server.http4s.Http4sServerInterpreter

object OnlyDocsMain extends IOApp.Simple {

  implicit def logger[F[_]: Sync]: Logger[F] = Slf4jLogger.getLogger

  override def run: IO[Unit] =
    main[IO].use { _ =>
      Logger[IO].info("Press enter to exit").flatMap(_ => IO.readLine)
    }.void

  private def main[F[_]: Async]: Resource[F, Server] =
    for {
      cfg <- Resource.eval(AppConfig.read)
      routes = Http4sServerInterpreter[F]().toRoutes(AppRouter.docRoutes[F])
      server <- BlazeServerBuilder[F]
        .bindHttp(cfg.server.port, cfg.server.host)
        .withHttpApp(Router("/" -> routes).orNotFound)
        .resource
    } yield server
}
