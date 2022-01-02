package Week1

import Yaml.YamlReader
import akka.actor.ActorSystem

object CounterActorExample extends App{
  val specification = YamlReader.readYamlFile("config.yaml")
  val actorSystemName = specification.get("ActorSystem").mkString
  val actorName = specification.get("Actor")
  println(actorName)

  //val actorSystem = ActorSystem(actorSystemName)

  println(specification)
}
