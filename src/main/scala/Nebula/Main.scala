package Nebula

import HelperUtils.{CreateLogger, ObtainConfigReference}
import Nebula.Compiler.ActorCompiler
import Nebula.Generator.ActorGenerator
import Nebula.Schema.ActorSchema

class Main

object Main extends  App{
  //Init the config file to get static params
  val config = ObtainConfigReference("nebula") match {
    case Some(value) => value
    case None => throw new RuntimeException("Cannot obtain a reference to the config data.")
  }

  //Init the logger
  val logger = CreateLogger(classOf[Main])

  val jsonSchema = ActorSchema.fromJson(config.getString("nebula.actorsJsonFile"))
  //val jsonSchema = YamlReader.yamlToJsonConverter(config.getString("treeHugger.yamlFile"))
  jsonSchema.foreach(actor => {
    val generatedCode : String = ActorGenerator.generateActor(actor)
    println(generatedCode)
    val compiledCode = ActorCompiler.compileCode(generatedCode)
    ActorCompiler.runCode(compiledCode)
  })

}
