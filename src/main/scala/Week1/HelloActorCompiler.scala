package Week1

import HelperUtils.{CreateLogger, ObtainConfigReference}
import akka.actor.{Props}

import scala.collection.mutable
import scala.reflect.runtime.currentMirror
import scala.reflect.runtime.universe._
import scala.tools.reflect.ToolBox

class HelloActorCompiler
object HelloActorCompiler {
  //Init the config file to get static params
  val config = ObtainConfigReference("helloActor") match {
    case Some(value) => value
    case None => throw new RuntimeException("Cannot obtain a reference to the config data.")
  }
  //Init the logger
  val logger = CreateLogger(classOf[HelloActorCompiler])

  //Create toolbox
  val toolbox = currentMirror.mkToolBox()

  def composeActors(configuration : mutable.Map[String, Any]) : Set[Props] = {

    Set()
  }
}
