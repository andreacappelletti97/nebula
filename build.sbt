val scala3Version = "3.1.1"
val jacksonModuleVersion = "2.13.1"
val apacheCommonLangVersion = "3.12.0"
val sfl4sVersion = "2.0.0-alpha5"
val typesafeConfigVersion = "1.4.1"
val scalaReflectVersion = "2.13.8"
val scalaCompilerVersion = "2.13.8"
val akkaActorVersion = "2.6.18"

lazy val root = project
  .in(file("."))
  .settings(
    name := "nebula",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    //Scala 3 dependencies
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor" % akkaActorVersion,
      "com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonModuleVersion,
      "org.apache.commons" % "commons-lang3" % apacheCommonLangVersion,
      "org.slf4j" % "slf4j-api" % sfl4sVersion,
      "com.typesafe" % "config" % typesafeConfigVersion,
    )
  ) dependsOn(nebula_scala2)


lazy val nebula_scala2 = project
  .in(file("nebula_scala2"))
  .settings(
    name := "nebula_scala2",
    scalaVersion := "2.13.3",
    //Scala 2 dependencies
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % scalaReflectVersion,
      "org.scala-lang" % "scala-compiler" % scalaCompilerVersion,
    )
  )




