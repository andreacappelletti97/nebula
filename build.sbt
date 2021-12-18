

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

// https://mvnrepository.com/artifact/com.typesafe.akka/akka-actor
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
  "org.scalatest" %% "scalatest" % "3.1.0" % Test
)
