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
```bash
cd docker
```

Run the dashboard

```bash
docker compose-up
```
Open a browser and go to the address

```bash
 localhost:3000/
```

## sbt project compiled with Scala 3

### Usage

This is a normal sbt project. You can compile code with `sbt compile`, run it with `sbt run`, and `sbt console` will start a Scala 3 REPL.

For more information on the sbt-dotty plugin, see the
[scala3-example-project](https://github.com/scala/scala3-example-project/blob/main/README.md).

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
