val scala3Version = "3.1.1"

ThisBuild / assemblyMergeStrategy := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case "reference.conf" => MergeStrategy.concat
  case x => MergeStrategy.first
}

val scalaLoggingVersion = "3.9.4"
val logBackVersion = "1.2.10"

run / cinnamon := true
test / cinnamon := true

lazy val root = project
  .in(file("."))
  .settings(
    name := "nebula",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      "com.novocode" % "junit-interface" % "0.11" % "test",
      "ch.qos.logback" % "logback-classic" % logBackVersion,
      "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion,
    )
  ).enablePlugins(Cinnamon) aggregate(nebula_scala2, nebula_scala3) dependsOn(nebula_scala2, nebula_scala3)


val jacksonModuleVersion = "2.13.1"
val apacheCommonLangVersion = "3.12.0"
val snakeYamlVersion = "1.30"

lazy val nebula_scala3 = project
  .in(file("nebula_scala3"))
  .settings(
    name := "nebula_scala3",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      "com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonModuleVersion,
      "org.apache.commons" % "commons-lang3" % apacheCommonLangVersion,
      "org.yaml" % "snakeyaml" % snakeYamlVersion
    )
  )


val scalaReflectVersion = "2.13.8"
val scalaCompilerVersion = "2.13.8"
val akkaActorVersion = "2.6.18"
//Kamon monitoring
val kamonBundleVersion = "2.4.7"
val kamonApmReporterVersion = "2.4.7"
val kamonPrometheusVersion = "2.4.7"

lazy val nebula_scala2 = project
  .in(file("nebula_scala2"))
  .settings(
    name := "nebula_scala2",
    scalaVersion := "2.13.3",
    //Scala 2 dependencies
    libraryDependencies ++= Seq(
      // Use Coda Hale Metrics and Akka instrumentation
      Cinnamon.library.cinnamonCHMetrics,
      Cinnamon.library.cinnamonJvmMetricsProducer,
      Cinnamon.library.cinnamonPrometheus,
      Cinnamon.library.cinnamonPrometheusHttpServer,
      // Use Akka instrumentation
      Cinnamon.library.cinnamonAkka,
      Cinnamon.library.cinnamonAkkaTyped,
      Cinnamon.library.cinnamonAkkaPersistence,
      Cinnamon.library.cinnamonAkkaStream,
      Cinnamon.library.cinnamonAkkaProjection,
      // Use Akka HTTP instrumentation
      Cinnamon.library.cinnamonAkkaHttp,
      // Use Akka gRPC instrumentation
      Cinnamon.library.cinnamonAkkaGrpc,
      "com.typesafe.akka" %% "akka-actor" % akkaActorVersion,
      "org.scala-lang" % "scala-reflect" % scalaReflectVersion,
      "org.scala-lang" % "scala-compiler" % scalaCompilerVersion,
      "io.kamon" %% "kamon-bundle" % kamonBundleVersion,
      "io.kamon" %% "kamon-apm-reporter" % kamonApmReporterVersion,
      "io.kamon" %% "kamon-prometheus" % kamonPrometheusVersion
    )
  ) dependsOn(nebula_scala3)

val jarName = "nebula.jar"
assembly/assemblyJarName := jarName
