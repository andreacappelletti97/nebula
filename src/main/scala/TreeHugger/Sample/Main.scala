package TreeHugger.Sample

import HelperUtils.{CreateLogger, ObtainConfigReference}
import TreeHugger.Generator.TreehuggerGenerator
import TreeHugger.Schema.TypeSchema

class Main
object Main extends App{
  //Init the config file to get static params
  val config = ObtainConfigReference("treeHugger") match {
    case Some(value) => value
    case None => throw new RuntimeException("Cannot obtain a reference to the config data.")
  }

  //Init the logger
  val logger = CreateLogger(classOf[Main])
  logger.info("Init the JSON schema...")
  val schema = TypeSchema.fromJson(config.getString("treeHugger.jsonFile"))
  logger.info("Parsing the JSON schema via TreeHugger...")
  println((new TreehuggerGenerator).generate(schema))
  logger.info("Parsing completed")
}
