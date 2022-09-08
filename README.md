# Nebula
Nebula automates the development effort to produce actor-based applications by burying the complexity of the specifics of the Akka libraries in the underlying framework that exposes high-level declarative constructs for configuration.
More in detail, it allows the business analysts to describe the high-level design of the system using a general declarative language such as Yaml or JSON. Specific business logic algorithms can be implemented using low-level code by developers or they can be deployed as separate packaged services.
Nebula translates the description of the system created by business analysts and automatically generates the application code at run-time based on the Akka framework that is created using the actor model. Moreover, Nebula integrates an external monitoring mechanism to enable stakeholders to detect performance problems with the deployed code. The generated code is deployed on the fly using the Scala Reflection module based on the distributed actor model implemented in the Akka framework.

## Authors
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
