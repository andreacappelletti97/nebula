package Nebula.Compiler

import HelperUtils.{CreateLogger, ObtainConfigReference}
import Nebula.Main.toolbox

import scala.reflect.runtime.universe.ClassDef

class CaseClassCompiler

object CaseClassCompiler{
  //Init the config file to get static params
  val config = ObtainConfigReference("nebula") match {
    case Some(value) => value
    case None => throw new RuntimeException("Cannot obtain a reference to the config data.")
  }
  //Init the logger
  val logger = CreateLogger(classOf[CaseClassCompiler])

  //Function to compile the code at runtime through Scala Reflection
  def defineCode(codeToCompile : String) = {
    logger.info("[defineCode]: init")
    val tree = toolbox.parse(codeToCompile)
    val classDefinition : ClassDef = tree.asInstanceOf[ClassDef]
    toolbox.define(classDefinition)
    logger.info("[defineCode]: exit")
  }


}
