package Nebula.Compiler

import HelperUtils.{CreateLogger, ObtainConfigReference}
import Nebula.Main.toolbox
import akka.actor.{ActorSystem, Props}


class ActorCompiler

object ActorCompiler {
  //Init the config file to get static params
  val config = ObtainConfigReference("nebula") match {
    case Some(value) => value
    case None => throw new RuntimeException("Cannot obtain a reference to the config data.")
  }
  //Init the logger
  val logger = CreateLogger(classOf[ActorCompiler])


  //Function to compile the code at runtime through Scala Reflection
  def compileCode(codeToCompile : String): Props = {
    logger.info("[compileCode]: init")
    val tree = toolbox.parse(codeToCompile)
    val binary = toolbox.compile(tree)()
    logger.info("[compileCode]: exit")
    binary.asInstanceOf[Props]
  }

  //Function to instantiate the Actor into an ActorSystem and send a message to it
  def runCode(compiledProps: Props): Unit = {
    logger.info("[runCode]: init")
    val actorSystem = ActorSystem("system")
    val helloActor = actorSystem.actorOf(compiledProps)
    helloActor ! None
    logger.info("[runCode]: exit")
  }

}