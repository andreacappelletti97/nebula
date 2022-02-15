package NebulaScala2

import akka.actor.Actor

class SimpleActor extends Actor {
  override def receive: Receive = {
    case _ => println("I have received something!")
    case "hello" =>
      Thread.sleep(2000) 
      sender() ! "getReference" 
  }
}

