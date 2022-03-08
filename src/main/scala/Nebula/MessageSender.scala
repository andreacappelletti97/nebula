package Nebula

import NebulaScala3.message.ProtoMessage
import akka.actor.{ActorSystem, Props}

class MessageSender

object MessageSender {
  def sendMessage(actor : Props, message: ProtoMessage): Unit = {
    val actorSystem = ActorSystem("system")
    val myActor = actorSystem.actorOf(actor)
    myActor ! message
  }
}
