// Enable the Lightbend Telemetry (Cinnamon) sbt plugin
//lazy val telemetry = project in file(".") enablePlugins(Cinnamon)

// Add the Cinnamon Agent for run and test
//run / cinnamon := true
//test / cinnamon := true

//mainClass := Some("com.example.AkkaQuickstart")

lazy val root = project
  .in(file("."))
  .settings(
    name := "nebula",
    version := "0.1",
    scalaVersion := "3.1.1"
  )

val logbackVersion = "1.3.0-alpha10"
val typesafeConfigVersion = "1.4.1"
val sfl4sVersion = "2.0.0-alpha5"
val snakeYamlVersion = "1.29"
val scalaCompilerVersion = "2.13.8"
val scalaReflectVersion = "2.13.8"
val scalaTestVersion = "3.2.11"
val logBackVersion = "1.2.3"
val akkaActorVersion = "2.6.18"
val jacksonModuleVersion = "2.13.1"
val gsonVersion = "2.8.9"
val treeHuggerVersion = "0.4.3"
val apacheCommonLangVersion = "3.12.0"

// https://mvnrepository.com/artifact/com.typesafe.akka/akka-actor

// https://mvnrepository.com/artifact/org.scala-lang/scala-reflect

libraryDependencies ++= Seq(
  /*
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
   */
  "com.typesafe.akka" %% "akka-actor" % akkaActorVersion,
  "ch.qos.logback" % "logback-classic" % logBackVersion,
  "org.scalatest" %% "scalatest" %  scalaTestVersion % Test,
  "org.scala-lang" % "scala-reflect" % scalaReflectVersion,
  "org.scala-lang" % "scala-compiler" % scalaCompilerVersion,
  "ch.qos.logback" % "logback-core" % logbackVersion,
  "ch.qos.logback" % "logback-classic" % logbackVersion,
  "com.typesafe" % "config" % typesafeConfigVersion,
  "org.slf4j" % "slf4j-api" % sfl4sVersion,
  "org.yaml" % "snakeyaml" % snakeYamlVersion,
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonModuleVersion,
  "com.google.code.gson" % "gson" % gsonVersion,
  "org.apache.commons" % "commons-lang3" % apacheCommonLangVersion
)
