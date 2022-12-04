package org.sibadi.auditing

import cats.effect._
import doobie.util.transactor.Transactor
import org.http4s.HttpRoutes
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.{Router, Server}
import org.sibadi.auditing.api.routes.AppRouter
import org.sibadi.auditing.configs.{AppConfig, DatabaseConfig, ServerConfig}
import org.sibadi.auditing.db.{Migrations, TeacherDAO}
import org.sibadi.auditing.service.{Authenticator, Service}
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

import scala.io.StdIn

object Main extends IOApp.Simple {

  implicit def logger[F[_]: Sync]: Logger[F] = Slf4jLogger.getLogger

  override def run: IO[Unit] =
    main[IO].use { server =>
      IO.blocking {
        println(s"Go to http://localhost:${server.address.getPort}/docs to open SwaggerUI. Press ENTER key to exit.")
        StdIn.readLine()
      }
    }.void

  private def main[F[_]: Async]: Resource[F, Server] =
    for {
      cfg           <- Resource.eval(AppConfig.read)
      _             <- Resource.eval(Migrations(cfg.database).migrate())
      transactor    <- createTransactor(cfg.database)
      teacherDao    <- Resource.pure(new TeacherDAO[F](transactor))
      service       <- Service(teacherDao)
      authenticator <- Authenticator[F]()
      router        <- AppRouter[F](authenticator, service)
      server        <- server[F](cfg.server, router.httpRoutes)
    } yield server

  private def createTransactor[F[_]: Async](cfg: DatabaseConfig): Resource[F, Transactor[F]] = {
    Resource.eval {
      Sync[F].blocking {
        Transactor.fromDriverManager[F](
          "org.postgresql.Driver",
          cfg.jdbcUrl, // connect URL (driver-specific)
          cfg.username, // user
          cfg.password // password
        )
      }
    }
  }

  private def server[F[_]: Async](cfg: ServerConfig, routes: HttpRoutes[F]): Resource[F, Server] =
    BlazeServerBuilder[F]
      .bindHttp(cfg.port, cfg.host)
      .withHttpApp(Router("/" -> routes).orNotFound)
      .resource
}
