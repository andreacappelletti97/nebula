package NebulaScala2.Compiler

import scala.tools.reflect.ToolBox

class MessageCodeCompiler

object MessageCodeCompiler {
  def compileCode(codeToCompile : String, toolbox:  ToolBox[scala.reflect.runtime.universe.type]): Unit ={
    val tree = toolbox.parse(codeToCompile)
    val messageClassCompiled =  toolbox.compile(tree)()
    println(messageClassCompiled)
  }
}
