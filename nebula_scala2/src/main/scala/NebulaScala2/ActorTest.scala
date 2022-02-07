package NebulaScala2

import akka.actor.{Actor, ActorSystem, Props}
class HelloActor extends Actor {
  def receive : Receive = {
    case "ciao" => println("Ciao!!!")
    case None => println("None received!")
  }
}
object HelloActor {
  def props() : Props = Props(new HelloActor())
}

object ActorTest {
  def runActor(): Unit = {
    val actorSystem = ActorSystem("system")
    val actor = actorSystem.actorOf(HelloActor.props())
    actor ! "ciao"
  }

}
