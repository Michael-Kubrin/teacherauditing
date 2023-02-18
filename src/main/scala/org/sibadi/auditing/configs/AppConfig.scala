package org.sibadi.auditing.configs

import cats.Show
import cats.effect.Sync
import pureconfig.ConfigSource
import pureconfig.generic.auto._

final case class AppConfig(
  server: ServerConfig,
  database: DatabaseConfig,
  admin: AdminConfig
)

object AppConfig {

  def read[F[_]: Sync]: F[AppConfig] =
    Sync[F].blocking {
      ConfigSource.default.loadOrThrow[AppConfig]
    }

  implicit val show: Show[AppConfig] = (t: AppConfig) => s"""
      |AppConfig:
      |  server:
      |    host: ${t.server.host}
      |    port: ${t.server.port}
      |  database:
      |    host: ${t.database.host}
      |    port: ${t.database.port}
      |    name: ${t.database.name}
      |    username: ${t.database.username}
      |    password: ${t.database.password}
      |    maxPoolSize: ${t.database.maxPoolSize}
      |  admin:
      |    bearer: ${t.admin.bearer}
      |""".stripMargin

}
