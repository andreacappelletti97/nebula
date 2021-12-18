
lazy val root = project
  .in(file("."))
  .settings(
    name := "nebula",
    version := "0.1",
    scalaVersion := "3.1.1-RC1-bin-20211007-c041327-NIGHTLY"
  )

// https://mvnrepository.com/artifact/com.typesafe.akka/akka-actor
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.6.17"


