package Nebula.Generator

import Nebula.Schema.ActorSystemSchema
import akka.actor.ActorSystem

object ActorSystemGenerator {

  def generateActorSystem(actorSystemJson : Seq[ActorSystemSchema], iterator : Int, actorSystemList : Seq[ActorSystem]): Seq[ActorSystem] = {
    if(iterator >= actorSystemJson.length) actorSystemList
    else {
      generateActorSystem(actorSystemJson, iterator + 1,
        actorSystemList :+ ActorSystem(actorSystemJson(iterator).actorSystemName)
      )
    }
  }

}
