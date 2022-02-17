#!/bin/bash

JAR="nebula.jar"
CinnamonAgent="/Users/andreacappelletti/Library/Caches/Coursier/v1/https/repo.lightbend.com/pass/O-2gx6jQ1VsfDWAktw1f-3ED1auXmKvxAnA24gnpUnTZpu2g/commercial-releases/com/lightbend/cinnamon/cinnamon-agent/2.16.2/cinnamon-agent-2.16.2.jar"

sbt clean assembly

java -javaagent:CinnamonAgent -jar nebula.jar
