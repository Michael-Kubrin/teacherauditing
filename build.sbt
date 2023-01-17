resolvers ++= Seq("jBCrypt Repository" at "https://repo1.maven.org/maven2/org/")

lazy val root = (project in file("."))
  .settings(
    scalaVersion := Versions.scala,
    name         := "teacherauditing",
    version      := "0.0.1",
    scalacOptions ++= Seq(
      //"-Xfatal-warnings",
      "-Wunused:imports,privates,locals,patvars",
      "-Wconf:src=src_managed/.*:silent",
      "-Wdead-code",
      "-Xlint:infer-any",
      "-feature",
      "-language:existentials",
      "-deprecation"
    ),
    semanticdbEnabled          := true,
    semanticdbVersion          := scalafixSemanticdb.revision,
    assembly / assemblyJarName := s"${name.value}-${version.value}.jar",
    assembly / mainClass       := Some("org.sibadi.auditing.Main"),
    assembly / assemblyMergeStrategy := {
      // https://tapir.softwaremill.com/en/latest/docs/openapi.html?highlight=assembly#using-swaggerui-with-sbt-assembly
      case PathList("META-INF", "maven", "org.webjars", "swagger-ui", "pom.properties") | PathList("META-INF", "resources", "webjars", _*) =>
        MergeStrategy.singleOrError
      // We should keep all configs from dependencies
      case PathList("reference.conf") => MergeStrategy.concat
      // Service Providers (defined in META-INF) are discarded by default
      //  https://stackoverflow.com/a/74212770
      //  https://github.com/sbt/sbt-assembly#exclude-specific-transitive-deps
      case PathList("META-INF", xs @ _*) =>
        xs.map(_.toLowerCase) match {
          case "services" :: xs =>
            MergeStrategy.filterDistinctLines
          case _ => MergeStrategy.discard
        }
      case _ => MergeStrategy.first
    },
    testFrameworks += new TestFramework("weaver.framework.CatsEffect"),
    libraryDependencies ++= Dependencies.all
  )
