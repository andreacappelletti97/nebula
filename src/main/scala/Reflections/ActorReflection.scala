package Reflections

import scala.reflect.runtime.currentMirror
import scala.reflect.runtime.universe._
import scala.tools.reflect.ToolBox

object ActorReflection extends App {
  val toolbox = currentMirror.mkToolBox()

  // Class instance
  val actorCode = q"""
  import akka.actor._
class HelloActor(myName: String) extends Actor {
  def receive = {
    case "hello" => println("hello from %s".format(myName))
    case _       => println("'huh?', said %s".format(myName))
  }
}
  val system = ActorSystem("HelloSystem")
  val helloActor = system.actorOf(Props(new HelloActor("Fred")), name = "helloactor")
  helloActor ! "hey"
"""
  val compiledCode = toolbox.compile(actorCode)()


}
