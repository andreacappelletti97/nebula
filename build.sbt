//Enable Cinnamon Telemetry Agent
run / cinnamon := true
test / cinnamon := true

//Here we define all the common dependencies among all the different projects and modules
val scalaTestVersion = "3.2.11"

Compile / PB.targets := Seq(
  scalapb.gen() -> (Compile / sourceManaged).value / "scalapb"
)

lazy val commonDependencies = Seq(
  "org.scalatest" %% "scalatest" % scalaTestVersion % Test
)

//Scala 3 Orchestrator Project
val scala3Version = "3.1.1"
val logBackVersion = "1.2.10"
val scalaLoggingVersion = "3.9.4"

lazy val root = project
  .in(file("."))
  .settings(
    name := "nebula",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies ++= commonDependencies ++ Seq(
      "com.novocode" % "junit-interface" % "0.11" % "test",
      "ch.qos.logback" % "logback-classic" % logBackVersion,
      "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion,
      "com.thesamet.scalapb" %% "scalapb-runtime" % "0.11.8",
      "com.github.os72" % "protobuf-dynamic" % "1.0.1"
    )
  ).enablePlugins(Cinnamon) aggregate(nebula_scala2, nebula_scala3) dependsOn(nebula_scala2, nebula_scala3)

//Scala 3 Submodule
val jacksonModuleVersion = "2.13.1"
val apacheCommonLangVersion = "3.12.0"
val snakeYamlVersion = "1.30"

lazy val nebula_scala3 = project
  .in(file("nebula_scala3"))
  .settings(
    name := "nebula_scala3",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies ++= commonDependencies ++ Seq(
      "com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonModuleVersion,
      "org.apache.commons" % "commons-lang3" % apacheCommonLangVersion,
      "org.yaml" % "snakeyaml" % snakeYamlVersion,
      "ch.qos.logback" % "logback-classic" % logBackVersion,
      "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion,
      "com.github.os72" % "protobuf-dynamic" % "1.0.1"
    )
  )

//Scala 2 Submodule
val scalaReflectVersion = "2.13.8"
val scalaCompilerVersion = "2.13.8"
val akkaActorVersion = "2.6.18"
val kamonBundleVersion = "2.4.7"
val kamonApmReporterVersion = "2.4.7"
val kamonPrometheusVersion = "2.4.7"

lazy val nebula_scala2 = project
  .in(file("nebula_scala2"))
  .settings(
    name := "nebula_scala2",
    scalaVersion := "2.13.3",
    //Scala 2 dependencies
    libraryDependencies ++= commonDependencies ++ Seq(
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
  ) dependsOn nebula_scala3

//SBT assembly properties
val jarName = "nebula.jar"
assembly/assemblyJarName := jarName

//Merging strategies
ThisBuild / assemblyMergeStrategy := {
  case PathList("META-INF", _*) => MergeStrategy.discard
  case "reference.conf" => MergeStrategy.concat
  case _ => MergeStrategy.first
}