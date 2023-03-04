package org.sibadi.auditing.db

import cats.effect.{Async, Resource, Sync}
import cats.syntax.flatMap._
import cats.syntax.applicativeError._
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import doobie.Transactor
import org.flywaydb.core.Flyway
import org.sibadi.auditing.configs.{AppConfig, DatabaseConfig}
import org.typelevel.log4cats.Logger

import javax.sql.DataSource

class DbComponent[F[_]](
  val transactor: Transactor[F],
  val teacherCredentialsDAO: TeacherCredentialsDAO[F],
  val reviewerCredentialsDAO: ReviewerCredentialsDAO[F]
)

object DbComponent {

  def apply[F[_]: Async: Logger](cfg: AppConfig): Resource[F, DbComponent[F]] =
    for {
      _          <- Resource.eval(migrate(cfg.database))
      transactor <- createTransactor(cfg.database)
      teacherCredentials  = new TeacherCredentialsDAO(transactor)
      reviewerCredentials = new ReviewerCredentialsDAO(transactor)
    } yield new DbComponent(transactor, teacherCredentials, reviewerCredentials)

  private def createTransactor[F[_]: Async](cfg: DatabaseConfig): Resource[F, Transactor[F]] =
    Resource.eval {
      Sync[F].blocking {
        Transactor.fromDriverManager[F](
          "org.postgresql.Driver",
          cfg.jdbcUrl,  // connect URL (driver-specific)
          cfg.username, // user
          cfg.password  // password
        )
      }
    }

  private def migrate[F[_]: Sync: Logger](cfg: DatabaseConfig): F[Unit] =
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
