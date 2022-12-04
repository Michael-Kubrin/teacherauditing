package org.sibadi.auditing.configs

import cats.effect.Sync
import pureconfig.ConfigSource
import pureconfig.generic.auto._

final case class AppConfig(
  server: ServerConfig,
  database: DatabaseConfig
)

object AppConfig {

  def read[F[_]: Sync]: F[AppConfig] =
    Sync[F].blocking {
      ConfigSource.default.loadOrThrow[AppConfig]
    }

}
