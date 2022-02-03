package Nebula

import scala.reflect.runtime.universe
import scala.reflect.runtime.universe.{ClassDef, ValDef}
import scala.tools.reflect.ToolBox

object Example extends App {
  val toolbox = universe.runtimeMirror(getClass.getClassLoader).mkToolBox()
  val myCode = toolbox.eval(toolbox.parse("case class Film(title: String) \n scala.reflect.classTag[Film].runtimeClass"))
  
  val actorCode =
    "import akka.actor._;" +
    "\n class myActor extends Actor {" +
    "\n def receive(): PartialFunction[Object,scala.runtime.BoxedUnit] = ???" +
    "\n def context(): akka.actor.ActorContext = ??? " +
    "\n def self(): akka.actor.ActorRef = ???" +
    "\n def akka$actor$Actor$_setter_$context_=(x$0: akka.actor.ActorContext): Unit = ???" +
    "\n def akka$actor$Actor$_setter_$self_=(x$0: akka.actor.ActorRef): Unit = ???" +
    "\n}"

  val myActor = toolbox.eval(toolbox.parse(
    actorCode
  ))


}
