package Nebula

import NebulaScala2.Scala2Main
import NebulaScala2.Scala2Main.generatedActorsRef
import akka.actor.ActorRef
object ActorRefBridge {
  def getActorRef(transitions : Seq[String], iterator: Int, actorRefList: Seq[ActorRef]) : Seq[ActorRef] = {
    if (iterator >= transitions.size) actorRefList
    else {
      generatedActorsRef.get(transitions(iterator)) match {
        case Some(actorRef) => getActorRef(transitions, iterator + 1, actorRefList ++ actorRef)
        case None => getActorRef(transitions, iterator + 1, actorRefList)
      }
    }
  }
}
