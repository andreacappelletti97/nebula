package Week1

import HelperUtils.{CreateLogger, ObtainConfigReference}
import akka.actor.ActorSystem

class HelloActor
object HelloActor extends App {
  //Init the config file to get static params
  val config = ObtainConfigReference("helloActor") match {
    case Some(value) => value
    case None => throw new RuntimeException("Cannot obtain a reference to the config data.")
  }
  //Init the logger
  val logger = CreateLogger(classOf[HelloActor])
  //Retrieve the yaml configuration file
  val yamlFile = config.getString("helloActor.yamlFile")
  //Get the configuration as Map[String, Any]
  val configuration = Yaml.YamlReader.readYamlFile(yamlFile)
  //Create the actorSystem
  val actorSystemName = configuration.get("ActorSystem").mkString
  val actorSystem = ActorSystem(actorSystemName)
  //Get the Set of Actors to instantiate
  val actorSet = HelloActorCompiler.composeActors(configuration)

  //Retrieve the instances
  val numOfInstances = configuration.getOrElse("Instances", null)
  println(numOfInstances)

}
