

// Enable the Lightbend Telemetry (Cinnamon) sbt plugin
lazy val telemetry = project in file(".") enablePlugins(Cinnamon)

// Add the Cinnamon Agent for run and test
run / cinnamon := true
test / cinnamon := true

//mainClass := Some("com.example.AkkaQuickstart")

lazy val root = project
  .in(file("."))
  .settings(
    name := "nebula",
    version := "0.1",
    scalaVersion := "3.1.1-RC1-bin-20211007-c041327-NIGHTLY"
  )

val logbackVersion = "1.3.0-alpha10"
val typesafeConfigVersion = "1.4.1"
val sfl4sVersion = "2.0.0-alpha5"

// https://mvnrepository.com/artifact/com.typesafe.akka/akka-actor

// https://mvnrepository.com/artifact/org.scala-lang/scala-reflect

libraryDependencies ++= Seq(
  // Use Coda Hale Metrics and Akka instrumentation
  Cinnamon.library.cinnamonCHMetrics,
  Cinnamon.library.cinnamonAkka,
  Cinnamon.library.cinnamonAkkaTyped,
  Cinnamon.library.cinnamonJvmMetricsProducer,
  Cinnamon.library.cinnamonPrometheus,
  Cinnamon.library.cinnamonPrometheusHttpServer,
  "com.typesafe.akka" %% "akka-actor" % "2.6.17",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "org.scalatest" %% "scalatest" % "3.1.0" % Test,
  "org.scala-lang" % "scala-reflect" % "2.13.7",
  "org.scala-lang" % "scala-compiler" % "2.13.7",
  "ch.qos.logback" % "logback-core" % logbackVersion,
  "ch.qos.logback" % "logback-classic" % logbackVersion,
  "com.typesafe" % "config" % typesafeConfigVersion,
  "org.slf4j" % "slf4j-api" % sfl4sVersion,

)
