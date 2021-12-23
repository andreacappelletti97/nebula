package Reflections


import HelperUtils.{CreateLogger, ObtainConfigReference}

import scala.reflect.runtime.currentMirror
import scala.reflect.runtime.universe._
import scala.tools.reflect.ToolBox

class BasicScalaReflection
object BasicScalaReflection extends App{

  //Init the config file to get static params
  val config = ObtainConfigReference("cinnamon") match {
    case Some(value) => value
    case None => throw new RuntimeException("Cannot obtain a reference to the config data.")
  }
  //Init the logger
  val logger = CreateLogger(classOf[BasicScalaReflection])

  //String init
  logger.info("Start of scala3+ reflection")
  val toolbox = currentMirror.mkToolBox()
  val code1 = q"""new String("hello")"""
  val result1 = toolbox.compile(code1)()
  println(result1)
  logger.info("End of scala3+ reflection")

  // Class instance
  val code2 = q"""
  case class A(name:String,age:Int){
    def f = (name,age)
    def who = println("my name is " + name)
  }
  val a = new A("Your Name",22)
  a.f
  """
  val result2 = toolbox.compile(code2)()
  println(result2)

}
