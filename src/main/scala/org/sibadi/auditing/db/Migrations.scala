package org.sibadi.auditing.db

import cats.effect.Sync
import cats.syntax.applicativeError._
import cats.syntax.flatMap._
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import org.flywaydb.core.Flyway
import org.sibadi.auditing.configs.DatabaseConfig
import org.typelevel.log4cats.Logger

import javax.sql.DataSource

class Migrations[F[_]: Sync: Logger](cfg: DatabaseConfig) {

  def migrate(): F[Unit] =
    Logger[F].info("Start database schema migration") >>
      Sync[F]
        .blocking {
          val hikariConfig = new HikariConfig()
          hikariConfig.setJdbcUrl(cfg.jdbcUrl)
          hikariConfig.setUsername(cfg.username)
          hikariConfig.setPassword(cfg.password)
          hikariConfig.setDriverClassName("org.postgresql.Driver")
          hikariConfig.setMaximumPoolSize(cfg.maxPoolSize)
          val dataSource: DataSource = new HikariDataSource(hikariConfig)
          Flyway
            .configure()
            .dataSource(dataSource)
            .baselineOnMigrate(true)
            .validateMigrationNaming(true)
            .load()
            .migrate()
        }
        .flatMap(c => Logger[F].info(s"Migrated $c scripts"))
        .handleErrorWith(e => Logger[F].error(e)(s"Migration failure: ${e.getMessage}") >> e.raiseError[F, Unit])

}

object Migrations {

  def apply[F[_]: Sync: Logger](connectionConfig: DatabaseConfig): Migrations[F] =
    new Migrations(connectionConfig)

}
