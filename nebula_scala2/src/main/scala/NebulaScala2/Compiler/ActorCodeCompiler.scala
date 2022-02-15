package NebulaScala2.Compiler

import NebulaScala2.SimpleActor
import scala.tools.reflect.ToolBox
import akka.actor.{Props, ActorRef, ActorSystem}

class ActorCodeCompiler

object ActorCodeCompiler {
  var reference : ActorRef = _

  def runCode(codeToRun : String, toolbox:  ToolBox[scala.reflect.runtime.universe.type]): Unit ={
    //Compile Actor code and get the Props object
    val tree = toolbox.parse(codeToRun)
    val actorProps =  toolbox.compile(tree)().asInstanceOf[Props]
    //Define ActorSystem
    val actorSystem = ActorSystem("system")

    val myActor = actorSystem.actorOf(Props[SimpleActor], "SimpleActor")
    reference = myActor
    val helloActor = actorSystem.actorOf(actorProps, "HelloActor")
    helloActor ! "getReference"
    //helloActor ! "sendMessage"
    //actorSystem.terminate()
  }

}

