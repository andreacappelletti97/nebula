package Nebula

import akka.actor.ActorSystem
import NebulaScala2.Scala2Main.generatedActorSystems

class ActorSystemFactory

object ActorSystemFactory {
  //This function is used from the Orchestrator to init actorSystems and store them
  def initActorSystem(name : String): Unit = {
    val actorSystem = ActorSystem(name)
    generatedActorSystems += name -> actorSystem
  }
}
