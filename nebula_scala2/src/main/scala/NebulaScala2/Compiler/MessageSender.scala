package NebulaScala2.Compiler

import akka.actor.{ActorSystem, Props}
import NebulaScala3.message.ProtoMessage

class MessageSender

object MessageSender {
  def sendMessage(actor : Props, message: ProtoMessage): Unit = {
    val actorSystem = ActorSystem("system")
    val myActor = actorSystem.actorOf(actor)
    myActor ! message
  }
}
