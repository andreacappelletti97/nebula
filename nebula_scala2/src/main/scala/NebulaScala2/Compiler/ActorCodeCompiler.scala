package NebulaScala2.Compiler

import akka.actor.{ActorSystem, Props}
import scala.tools.reflect.ToolBox
import NebulaScala3.Scala3Main.theKeeper

class ActorCodeCompiler

object ActorCodeCompiler {

  def runCode(codeToRun : String, toolbox:  ToolBox[scala.reflect.runtime.universe.type]): Unit ={
    val tree = toolbox.parse(codeToRun)
    val actorProps =  toolbox.compile(tree)().asInstanceOf[Props]
    val actorSystem = ActorSystem("system")
    val helloActor = actorSystem.actorOf(actorProps)
    helloActor ! "changeX"
    //actorSystem.terminate()
  }

}

