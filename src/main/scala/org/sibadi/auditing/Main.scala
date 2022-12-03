package org.sibadi.auditing

import cats.effect.{IO, IOApp, Sync}
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.Router
import org.sibadi.auditing.routes.Router
import org.sibadi.auditing.service.Authenticator
import org.typelevel.log4cats.SelfAwareStructuredLogger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import sttp.tapir.server.http4s.Http4sServerInterpreter

import scala.io.StdIn

object Main extends IOApp.Simple {

  implicit def unsafeLogger[F[_]: Sync]: SelfAwareStructuredLogger[F] = Slf4jLogger.getLogger

  val authenticator = new Authenticator[IO]
  val router = new Router[IO](authenticator)

  val routes = Http4sServerInterpreter[IO]().toRoutes(router.routes)

  override def run: IO[Unit] =
    BlazeServerBuilder[IO]
      .bindHttp(9999, "localhost")
      .withHttpApp(Router("/" -> routes).orNotFound)
      .resource
      .use { server =>
        IO.blocking {
          println(s"Go to http://localhost:${server.address.getPort}/docs to open SwaggerUI. Press ENTER key to exit.")
          StdIn.readLine()
        }
      }
      .void
}
