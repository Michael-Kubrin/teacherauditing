lazy val root = (project in file("."))
  .settings(
    scalaVersion := Versions.scala,
    name         := "teacherauditing",
    version      := "0.0.1",
    scalacOptions ++= Seq(
      "-Xfatal-warnings",
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
      case PathList("META-INF", "maven", "org.webjars", "swagger-ui", "pom.properties") | PathList("META-INF", "resources", "webjars", _*) =>
        MergeStrategy.singleOrError
      case PathList("reference.conf") => MergeStrategy.concat
      case PathList("META-INF", _*)   => MergeStrategy.discard
      case _                          => MergeStrategy.first
    },
    testFrameworks += new TestFramework("weaver.framework.CatsEffect"),
    libraryDependencies ++= Dependencies.all
  )
