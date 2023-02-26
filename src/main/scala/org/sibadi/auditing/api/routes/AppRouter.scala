package org.sibadi.auditing.api.routes

import cats.effect.{Async, Sync}
import org.http4s.HttpRoutes
import org.sibadi.auditing.api.endpoints.{AppEndpoints, YamlDocAPI}
import sttp.apispec.openapi.OpenAPI
import sttp.apispec.openapi.circe.yaml._
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter

class AppRouter[F[_]: Async]() {

  def httpRoutes: HttpRoutes[F] =
    Http4sServerInterpreter[F]().toRoutes(allRoutes ++ AppRouter.docRoutes :+ docAsYamlRoute)

  def docAsYamlRoute =
    YamlDocAPI.yamlDocAPIEndpoint
      .serverLogicSuccess { _ =>
        Sync[F].delay {
          val docs: OpenAPI = OpenAPIDocsInterpreter().toOpenAPI(AppEndpoints.allEndpoints, "Teacher Auditing API", "1.0")
          docs.toYaml
        }
      }

  private def allRoutes: List[ServerEndpoint[Any, F]] = List.empty

}

object AppRouter {

  def docRoutes[F[_]]: List[ServerEndpoint[Any, F]] =
    SwaggerInterpreter().fromEndpoints[F](
      AppEndpoints.allEndpoints,
      "Оценка показателей эффективности API",
      "1.0.0"
    )

}
