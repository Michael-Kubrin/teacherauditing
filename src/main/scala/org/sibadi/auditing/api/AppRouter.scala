package org.sibadi.auditing.api

import cats.effect.{Async, Sync}
import org.http4s.HttpRoutes
import org.sibadi.auditing.api.AppRouter.allEndpoints
import org.sibadi.auditing.api.endpoints._
import sttp.apispec.openapi.OpenAPI
import sttp.apispec.openapi.circe.yaml._
import sttp.tapir._
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter

class AppRouter[F[_]: Async](routes: List[ServerEndpoint[Any, F]]) {

  def httpRoutes: HttpRoutes[F] =
    Http4sServerInterpreter[F]().toRoutes(routes ++ AppRouter.docRoutes :+ docAsYamlRoute)

  private def docAsYamlRoute =
    baseEndpoint.get
      .tag("Docs")
      .in("api" / "docs.yaml")
      .out(stringBody)
      .out(header("Content-Type", "application/yaml"))
      .description("Список всех запросов")
      .serverLogicSuccess { _ =>
        Sync[F].delay {
          val docs: OpenAPI = OpenAPIDocsInterpreter().toOpenAPI(allEndpoints, "Teacher Auditing API", "1.0")
          docs.toYaml
        }
      }

}

object AppRouter {

  def docRoutes[F[_]]: List[ServerEndpoint[Any, F]] =
    SwaggerInterpreter().fromEndpoints[F](
      allEndpoints,
      "Оценка показателей эффективности API",
      "1.0.0"
    )

  private def allEndpoints = FullApi.all ++ AdminAPI.all

}
