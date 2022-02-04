package NebulaScala2.Compiler

import scala.reflect.runtime.universe
import scala.tools.reflect.ToolBox

object ToolboxGenerator {
  //This functions generate the Scala Toolbox and return it to Scala3 APIs
  def generateToolbox(): ToolBox[scala.reflect.runtime.universe.type] ={
    val toolbox = universe.runtimeMirror(getClass.getClassLoader).mkToolBox()
    toolbox
  }
}
