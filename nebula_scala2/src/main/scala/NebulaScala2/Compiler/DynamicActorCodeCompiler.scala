package NebulaScala2.Compiler

import akka.actor.Props

import scala.tools.reflect.ToolBox

class DynamicActorCodeCompiler

object DynamicActorCodeCompiler {
  //This funciton compile the Scala string code and returns the Actors props as a Seq
  def compileActors(actorCodeList : Seq[String] ,toolbox:  ToolBox[scala.reflect.runtime.universe.type], iterator: Int, compiledActorsList : Seq[Props]) : Seq[Props] = {
    if(iterator >= actorCodeList.size) compiledActorsList
    else {
      val tree = toolbox.parse(actorCodeList(iterator))
      val actorProps = toolbox.compile(tree)().asInstanceOf[Props]
      compileActors(actorCodeList, toolbox, iterator + 1, compiledActorsList :+ actorProps)
    }
  }
}
