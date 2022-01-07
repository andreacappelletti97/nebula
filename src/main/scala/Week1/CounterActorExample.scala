package Week1

import Yaml.YamlReader
import akka.actor.ActorSystem

class CounterActorExample

case class MyActor(Name: String, Case: List[String], Code: List[String])

object CounterActorExample extends App{
  val specification = YamlReader.readYamlFile("HelloActor.yaml")
  val actorSystemName = specification.get("ActorSystem").mkString
  val actorName = specification.get("Actor").mkString
  println(actorSystemName)

  //val actorSystem = ActorSystem(actorSystemName)
  specification.get("Actor").foreach(k => println(k))

  //println(specification)
}
