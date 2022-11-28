package org.sibadi.auditing.db

import cats.effect.Sync
import cats.syntax.applicativeError._
import cats.syntax.flatMap._
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import org.flywaydb.core.Flyway
import org.sibadi.auditing.configs.DatabaseConfig
import org.typelevel.log4cats.Logger

import javax.sql.DataSource
import scala.util.Try

class DatabaseMigration[F[_]: Sync: Logger](cfg: DatabaseConfig) {

  private val hikariConfig = new HikariConfig()

  hikariConfig.setJdbcUrl(cfg.jdbcUrl)
  hikariConfig.setUsername(cfg.username)
  hikariConfig.setPassword(cfg.password)
  hikariConfig.setDriverClassName(cfg.driver)
  cfg.maxPoolSize.foreach(hikariConfig.setMaximumPoolSize)
  cfg.schemaName.foreach(hikariConfig.setSchema)

  val dataSource: DataSource = new HikariDataSource(hikariConfig)

  def migrate(): F[Unit] =
    Logger[F].info("Start database schema migration") >>
      Sync[F]
        .fromTry(Try(Flyway.configure().dataSource(dataSource).baselineOnMigrate(true).load().migrate()))
        .flatMap(c => Logger[F].info(s"Migrated $c scripts"))
        .handleErrorWith(e => Logger[F].error(s"Migration failure: ${e.getMessage}"))

}

object DatabaseMigration {
  def apply[F[_]: Sync: Logger](connectionConfig: DatabaseConfig): DatabaseMigration[F] =
    new DatabaseMigration(connectionConfig)
}
