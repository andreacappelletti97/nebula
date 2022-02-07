val scala3Version = "3.1.1"

ThisBuild / assemblyMergeStrategy := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}

val sfl4sVersion = "2.0.0-alpha5"

lazy val root = project
  .in(file("."))
  .settings(
    name := "nebula",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    run / cinnamon := true,
    test / cinnamon := true,
    libraryDependencies ++= Seq(
      "com.novocode" % "junit-interface" % "0.11" % "test",
      "org.slf4j" % "slf4j-api" % sfl4sVersion,
    )
  ).enablePlugins(Cinnamon) aggregate(nebula_scala2, nebula_scala3) dependsOn(nebula_scala2, nebula_scala3)


val jacksonModuleVersion = "2.13.1"
val apacheCommonLangVersion = "3.12.0"

lazy val nebula_scala3 = project
  .in(file("nebula_scala3"))
  .settings(
    name := "nebula_scala3",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      "com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonModuleVersion,
      "org.apache.commons" % "commons-lang3" % apacheCommonLangVersion
    )
  )


val scalaReflectVersion = "2.13.8"
val scalaCompilerVersion = "2.13.8"
val akkaActorVersion = "2.6.18"

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
    )
  )

val jarName = "nebula.jar"
assembly/assemblyJarName := jarName
