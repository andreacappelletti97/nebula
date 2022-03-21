package Nebula

import NebulaScala3.message.ProtoMessage
import akka.actor.{ActorSystem, Props, ActorRef}

class MessageSender

object MessageSender {
  def sendMessage(actor : ActorRef, message: ProtoMessage): Unit = {
    println("SEND MESSAGE!!!")
    actor ! message
  }
}
