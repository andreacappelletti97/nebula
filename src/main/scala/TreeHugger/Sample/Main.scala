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
  val schemaArray = ActorSchema.fromJson(config.getString("treeHugger.jsonFile"))
  logger.info("Parsing the JSON schema via TreeHugger...")
  //schemaArray.foreach(element => println((new TreehuggerGenerator).generate(element)))
  logger.info("Parsing completed")

  val myCode : String = (new TreehuggerGenerator).generate(schemaArray(0))

  println(myCode)
  val toolbox = currentMirror.mkToolBox()
  val tree = toolbox.parse(myCode)
  val binary = toolbox.compile(tree)()
  println(binary)
  val myProps = binary.asInstanceOf[Props]

  val actorSystem = ActorSystem("firstActorSystem")
  val helloActor = actorSystem.actorOf(myProps)
  helloActor ! None

}

