import sbt._

object Versions {
  val cats              = "2.9.0"
  val catsEffect        = "3.4.8"
  val catsMtl           = "1.3.0"
  val fs2               = "3.6.1"
  val log4cats          = "2.5.0"
  val logback           = "1.4.5"
  val scala             = "2.13.8"
  val testcontainers    = "1.17.6"
  val weaver            = "0.8.1"
  val tapir             = "1.2.9"
  val tapirHttp4s       = "0.23.13"
  val doobie            = "1.0.0-RC2"
  val flyWay            = "9.15.0"
  val pureConfigVersion = "0.17.2"
}

object Dependencies {

  val catsCore       = "org.typelevel"               %% "cats-core"               % Versions.cats
  val catsEffect     = "org.typelevel"               %% "cats-effect"             % Versions.catsEffect
  val catsFree       = "org.typelevel"               %% "cats-free"               % Versions.cats
  val catsMtl        = "org.typelevel"               %% "cats-mtl"                % Versions.catsMtl
  val fs2Core        = "co.fs2"                      %% "fs2-core"                % Versions.fs2
  val fs2Io          = "co.fs2"                      %% "fs2-io"                  % Versions.fs2
  val log4catsCore   = "org.typelevel"               %% "log4cats-core"           % Versions.log4cats
  val log4catsSlf4f  = "org.typelevel"               %% "log4cats-slf4j"          % Versions.log4cats
  val logback        = "ch.qos.logback"               % "logback-classic"         % Versions.logback
  val testcontainers = "org.testcontainers"           % "testcontainers"          % Versions.testcontainers
  val weaver         = "com.disneystreaming"         %% "weaver-cats"             % Versions.weaver
  val tapir          = "com.softwaremill.sttp.tapir" %% "tapir-http4s-server"     % Versions.tapir
  val tapirHttp      = "org.http4s"                  %% "http4s-blaze-server"     % Versions.tapirHttp4s
  val tapirSwagger   = "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % Versions.tapir
  val tapirJson      = "com.softwaremill.sttp.tapir" %% "tapir-json-circe"        % Versions.tapir
  val tapirApiSpec      = "com.softwaremill.sttp.apispec" %% "openapi-circe-yaml"    % "0.3.2"
  val tapirEnumerantum = "com.softwaremill.sttp.tapir" %% "tapir-enumeratum" % Versions.tapir
  val doobieCore     = "org.tpolecat"                %% "doobie-core"             % Versions.doobie
  val doobieHikari   = "org.tpolecat"                %% "doobie-hikari"           % Versions.doobie
  val doobiePostgres = "org.tpolecat"                %% "doobie-postgres"         % Versions.doobie
  val flyWay         = "org.flywaydb"                 % "flyway-core"             % Versions.flyWay
  val pureConfig     = "com.github.pureconfig"       %% "pureconfig"              % Versions.pureConfigVersion
  val enumeratum    = "com.beachape" %% "enumeratum" % "1.7.2"

  val all: Seq[ModuleID] =
    Seq(
      catsCore,
      catsEffect,
      catsFree,
      catsMtl,
      fs2Core,
      fs2Io,
      log4catsCore,
      log4catsSlf4f,
      logback,
      tapir,
      tapirHttp,
      tapirSwagger,
      tapirJson,
      tapirApiSpec,
      tapirEnumerantum,
      doobieCore,
      doobieHikari,
      doobiePostgres,
      flyWay,
      pureConfig,
      enumeratum
    ) ++ Seq(
      testcontainers,
      weaver
    ).map(_ % "test")

}
