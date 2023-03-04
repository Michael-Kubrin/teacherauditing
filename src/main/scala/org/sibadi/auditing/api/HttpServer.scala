package org.sibadi.auditing.api

import cats.effect.{Async, Resource}
import org.http4s.HttpRoutes
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.middleware.CORS
import org.http4s.server.{Router, Server}
import org.sibadi.auditing.configs.ServerConfig

object HttpServer {

  def server[F[_]: Async](cfg: ServerConfig, routes: HttpRoutes[F]): Resource[F, Server] = {
    val cors = CORS.policy.withAllowOriginAll
      .withAllowCredentials(false)
      .apply(routes)
    BlazeServerBuilder[F]
      .bindHttp(cfg.port, cfg.host)
      .withHttpApp(Router("/" -> cors).orNotFound)
      .resource
  }

}
