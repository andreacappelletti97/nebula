package Nebula

import akka.actor.{ActorSystem, Props, ActorRef}
import NebulaScala2.Scala2Main.generatedActorsRef

class ActorFactory

object ActorFactory {
  //This function is used from the Orchestrator to init Actors and store them
  def initActor(actorSystem : ActorSystem, actorProps : Props, actorName: String): ActorRef = {
    val actor = actorSystem.actorOf(actorProps, actorName)
    actor
  }
}
