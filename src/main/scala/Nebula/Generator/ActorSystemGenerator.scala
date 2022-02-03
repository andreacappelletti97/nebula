package Nebula.Generator

import HelperUtils.{CreateLogger, ObtainConfigReference}
import Nebula.Schema.ActorSystemSchema
import akka.actor.ActorSystem

class ActorSystemGenerator
object ActorSystemGenerator {
  //Init the config file to get static params
  val config = ObtainConfigReference("nebula") match {
    case Some(value) => value
    case None => throw new RuntimeException("Cannot obtain a reference to the config data.")
  }
  //Init the logger
  val logger = CreateLogger(classOf[ActorSystemGenerator])

  def generateActorSystem(actorSystemJson : Seq[ActorSystemSchema], iterator : Int, actorSystemList : Seq[ActorSystem]): Seq[ActorSystem] = {
    if (iterator >= actorSystemJson.length) actorSystemList
    else generateActorSystem(actorSystemJson, iterator + 1, actorSystemList :+ ActorSystem(actorSystemJson(iterator).actorSystemName))
  }
}
