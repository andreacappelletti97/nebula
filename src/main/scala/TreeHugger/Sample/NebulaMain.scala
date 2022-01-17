package TreeHugger.Sample

import HelperUtils.{CreateLogger, ObtainConfigReference}
import TreeHugger.Generator.CodeGenerator
import TreeHugger.Schema.ActorTemplate
import Yaml.YamlReader

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
  })


}
