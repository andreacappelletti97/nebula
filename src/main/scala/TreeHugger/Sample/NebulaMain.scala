package TreeHugger.Sample

import HelperUtils.{CreateLogger, ObtainConfigReference}
import TreeHugger.Generator.CodeGenerator
import TreeHugger.Schema.ActorTemplate
import Yaml.YamlReader
import akka.actor.{ActorSystem, Props}

import scala.reflect.runtime.currentMirror
import scala.tools.reflect.ToolBox

class NebulaMain

object NebulaMain extends App {
  //Init the config file to get static params
  val config = ObtainConfigReference("treeHugger") match {
    case Some(value) => value
    case None => throw new RuntimeException("Cannot obtain a reference to the config data.")
  }

  //Init the logger
  val logger = CreateLogger(classOf[NebulaMain])
  val jsonSchema = ActorTemplate.fromJson(config.getString("treeHugger.jsonFile"))
  //val jsonSchema = YamlReader.yamlToJsonConverter(config.getString("treeHugger.yamlFile"))
  jsonSchema.foreach(actor => {
    val generatedCode : String = CodeGenerator.generate(actor)
    println(generatedCode)
    val compiledCode = compileCode(generatedCode)
    runCode(compiledCode)

  })

  //Function to compile the code at runtime through Scala Reflection
  def compileCode(codeToCompile : String): Props = {
    logger.info("[compileCode]: init")
    val toolbox = currentMirror.mkToolBox()
    val tree = toolbox.parse(codeToCompile)
    val binary = toolbox.compile(tree)()
    println(binary)
    logger.info("[compileCode]: exit")
    binary.asInstanceOf[Props]
  }

  //Function to instantiate the Actor into an ActorSystem and send a message to it
  def runCode(compiledProps: Props): Unit = {
    val actorSystem = ActorSystem("system")
    val helloActor = actorSystem.actorOf(compiledProps)
    helloActor ! None
  }
}
