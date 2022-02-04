package Compiler

import scala.reflect.runtime._
import scala.reflect.runtime.universe._
import scala.tools.reflect.ToolBox

class ActorCodeCompiler

object ActorCodeCompiler{

  def runReflection(): Unit = {
    val cm = universe.runtimeMirror(getClass.getClassLoader)
    val toolBox = cm.mkToolBox()
    val caseClassCode = q"""case class Authentication(email:String)""".asInstanceOf[ImplDef]
    val definedCaseClass = toolBox.define(caseClassCode)
    println(definedCaseClass)
  }

}
