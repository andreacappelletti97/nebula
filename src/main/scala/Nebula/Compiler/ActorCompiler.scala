package Nebula.Compiler

import HelperUtils.{CreateLogger, ObtainConfigReference}
import Nebula.Generator.{ActorCodeGenerator}
import Nebula.Main.toolbox
import Nebula.Schema.ActorSchema
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
    val actorProps =  toolbox.compile(tree)()
    println(actorProps)
    logger.info("[compileCode]: exit")
    null
  }


  def getCompiledActors(actorJson : Array[ActorSchema], iterator: Int, propsList : Seq[Props] ): Seq[Props] = {
    if(iterator >= actorJson.length) propsList
    else {
      //Generate the Scala code from an ActorSchema
      val generatedCode : String = ActorCodeGenerator.generateActor(actorJson(iterator))
      //Compile the code and get the Props
      val tree = toolbox.parse(generatedCode)
      val binary = toolbox.compile(tree)()
      getCompiledActors(actorJson, iterator + 1,
        propsList :+ binary.asInstanceOf[Props]
      )
    }
  }

  //Function to instantiate the Actor into an ActorSystem and send a message to it
  def runCode(compiledProps: Props, message : Any): Unit = {
    logger.info("[runCode]: init")
    val actorSystem = ActorSystem("system")
    val helloActor = actorSystem.actorOf(compiledProps)
    helloActor ! message
    actorSystem.terminate()
    logger.info("[runCode]: exit")
  }

}
