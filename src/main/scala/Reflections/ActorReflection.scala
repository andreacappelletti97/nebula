package Reflections

import akka.actor.{Actor, ActorSystem, Props}

import scala.reflect.runtime.currentMirror
import scala.reflect.runtime.universe._
import scala.tools.reflect.ToolBox

abstract class CompiledActor(myName: String) extends Actor{
  def receive: Receive
}

object ActorReflection extends App {
  val toolbox = currentMirror.mkToolBox()

  // Class instance
  val actorCode = q"""
  import akka.actor._
  object HelloActor {
   def props(name : String) = Props(new HelloActor(name))
  }
class HelloActor(myName: String) extends Actor {
  def receive = {
    case "hello" => println("hello from %s".format(myName))
    case _       => println("'huh?', said %s".format(myName))
  }
}
  //val system = ActorSystem("HelloSystem")
  //val helloActor = system.actorOf(Props(new HelloActor("Fred")), name = "helloactor")
  return HelloActor.props("Bobo")
"""
  val compiledCode = toolbox.compile(actorCode)()
  println(compiledCode)
  val actorSystem = ActorSystem("firstActorSystem")
  val myProps = compiledCode.asInstanceOf[Props]
  val helloActor = actorSystem.actorOf(myProps)
  helloActor ! "hello"

 // val myActor = compiledCode.asInstanceOf[CompiledActor]
  //println(myActor)


}
