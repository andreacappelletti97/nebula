//Enable Cinnamon Telemetry Agent
run / cinnamon := true
test / cinnamon := true

//Here we define all the common dependencies among all the different projects and modules
val scalaTestVersion = "3.2.11"

lazy val commonDependencies = Seq(
  "org.scalatest" %% "scalatest" % scalaTestVersion % Test
)

//Scala 3 Orchestrator Project
val scala3Version = "3.1.1"
val logBackVersion = "1.2.11"
val scalaLoggingVersion = "3.9.4"
val scalaSwingVersion = "3.0.0"

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
      "org.scala-lang.modules" %% "scala-swing" % scalaSwingVersion
    )
  ).enablePlugins(Cinnamon) aggregate(nebula_scala2, nebula_scala3) dependsOn(nebula_scala2, nebula_scala3)

//Scala 3 Submodule
val jacksonModuleVersion = "2.13.1"
val apacheCommonLangVersion = "3.12.0"
val snakeYamlVersion = "1.30"
val scalapbVersion = "0.11.8"

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
      "com.thesamet.scalapb" %% "scalapb-runtime" % scalapbVersion % "protobuf",
    ),
    Compile / PB.targets := Seq(
      scalapb.gen() -> (Compile / sourceManaged).value / "scalapb"
    )
  )

//Scala 2 Submodule
val scalaReflectVersion = "2.13.8"
val scalaCompilerVersion = "2.13.8"
val akkaVersion = "2.6.19"
val kamonBundleVersion = "2.5.1"
val kamonApmReporterVersion = "2.4.7"
val kamonPrometheusVersion = "2.5.1"
val scalaParallelCollectionVersion = "1.0.4"
val cassandraVersion = "1.0.5"

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
      "com.typesafe.akka" %% "akka-actor" % akkaVersion,
      //Akka cluster
      "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
      //Akka cluster sharding
      "com.typesafe.akka" %% "akka-cluster-sharding" % akkaVersion,
      "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
      //Akka persistence
      "com.typesafe.akka" %% "akka-persistence" % akkaVersion,
      "com.typesafe.akka" %% "akka-persistence-query" % akkaVersion,
      // Scala Reflection and Compiler
      "org.scala-lang" % "scala-reflect" % scalaReflectVersion,
      "org.scala-lang" % "scala-compiler" % scalaCompilerVersion,
      //Kamon Instrumentation
      "io.kamon" %% "kamon-bundle" % kamonBundleVersion,
      "io.kamon" %% "kamon-apm-reporter" % kamonApmReporterVersion,
      "io.kamon" %% "kamon-prometheus" % kamonPrometheusVersion,
      //Scala Parallel Collections
      "org.scala-lang.modules" %% "scala-parallel-collections" % scalaParallelCollectionVersion,
      // Cassandra
      "com.typesafe.akka" %% "akka-persistence-cassandra" % cassandraVersion,
      "com.typesafe.akka" %% "akka-persistence-cassandra-launcher" % cassandraVersion % Test,
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