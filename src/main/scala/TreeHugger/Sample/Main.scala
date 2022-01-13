package TreeHugger.Sample

import HelperUtils.{CreateLogger, ObtainConfigReference}
import TreeHugger.Generator.TreehuggerGenerator
import TreeHugger.Schema.ActorSchema
import akka.actor.{ActorSystem, Props}

import scala.reflect.runtime.currentMirror
import scala.tools.reflect.ToolBox

class Main
object Main extends App{
  //Init the config file to get static params
  val config = ObtainConfigReference("treeHugger") match {
    case Some(value) => value
    case None => throw new RuntimeException("Cannot obtain a reference to the config data.")
  }

  //Init the logger
  val logger = CreateLogger(classOf[Main])

  logger.info("Init the JSON array schema...")
  val jsonSchema = ActorSchema.fromJson(config.getString("treeHugger.jsonFile"))

  logger.info("[generateCode]: Parsing the JSON schema via TreeHugger...")
  jsonSchema.foreach(actor => {
    val generatedCode : String = (new TreehuggerGenerator).generate(actor)
    logger.info("New Actor code generated")
    println(generatedCode)
  })
  logger.info("[generateCode]: Parsing completed")

  //Function to compile the code at runtime through Scala Reflection
  def compileCode(codeToCompile : String): Unit = {
    logger.info("[compileCode]: init")
    val toolbox = currentMirror.mkToolBox()
    val tree = toolbox.parse(codeToCompile)
    val binary = toolbox.compile(tree)()
    println(binary)
    logger.info("[compileCode]: exit")
  }

  //Function to instantiate the Actor into an ActorSystem and send a message to it
  def runCode(compiledProps: Any): Unit = {
    val myProps = compiledProps.asInstanceOf[Props]
    val actorSystem = ActorSystem("system")
    val helloActor = actorSystem.actorOf(myProps)
    helloActor ! None
  }

}

