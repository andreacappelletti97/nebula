
import HelperUtils.{CreateLogger, ObtainConfigReference}

import scala.reflect.runtime.universe._
import scala.reflect.runtime.currentMirror
import scala.tools.reflect.ToolBox

class Reflection
object Reflection extends App{

  //Init the config file to get static params
  val config = ObtainConfigReference("cinnamon") match {
    case Some(value) => value
    case None => throw new RuntimeException("Cannot obtain a reference to the config data.")
  }
  //Init the logger
  val logger = CreateLogger(classOf[Reflection])

  logger.info("First scala3+ reflection")
  val toolbox = currentMirror.mkToolBox()
  val code1 = q"""new String("hello")"""
  val result1 = toolbox.compile(code1)()
  println(result1)
  logger.info("End of scala3+ reflection")

}
