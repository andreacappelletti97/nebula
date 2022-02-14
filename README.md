# Nebula
Dynamic Generation and Management of Actor-Based Applications

## Author
Andrea Cappelletti  
andreacappelletti97@gmail.com

Dr. Mark Grechanik  
drmark@uic.edu

## cinnamon telemetry
Prerequisites
- Docker

Before running our Cinnamon Instrumentation we have to install Docker
https://docs.docker.com/get-docker/

Once installed, from the root of the project type

```bash
cd docker
```

Run the dashboard

```bash
docker-compose up
```
Open a browser and go to the address

```bash
 localhost:3000/
```

## sbt project compiled with Scala 3

### Usage

This is a normal sbt project. You can compile code with `sbt compile`, run it with `sbt run`, and `sbt console` will start a Scala 3 REPL.

## sbt assembly
In order to assembly the fat jar, run this command in the root of the project
```bash
sbt clean assembly
```

You will find the generated jar under `target/scala-3.1.1/` with the name of  `nebula.jar`

To run it type

```bash
java -jar nebula.jar
```

In order to trigger the Cinnamon monitoring instrumentation run this command specifying the -javaagent.
```bash
java -javaagent:/path/to/cinnamon-agent.jar -jar nebula.jar
```
Where `/path/to/cinnamon-agent.jar` is the path to the jar in your machine.

In my case
```bash
/Users/andreacappelletti/Library/Caches/Coursier/v1/https/repo.lightbend.com/pass/O-2gx6jQ1VsfDWAktw1f-3ED1auXmKvxAnA24gnpUnTZpu2g/commercial-releases/com/lightbend/cinnamon/cinnamon-agent/2.16.2/cinnamon-agent-2.16.2.jar
```

Cinnamon instrumentation gets triggered automatically when using `sbt run` because the -javaagent is implicit into the command.