package NebulaScala2

import akka.actor.Actor

class SimpleActor extends Actor {
  override def receive: Receive = {
    case "hello" => println("Hello!")
    case _ =>
      println("I have received something!")
    //Thread.sleep(2000)
    //sender() ! "getReference"
  }
}



