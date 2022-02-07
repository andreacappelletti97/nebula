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
