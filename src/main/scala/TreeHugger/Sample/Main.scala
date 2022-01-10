package TreeHugger.Sample

import HelperUtils.ObtainConfigReference
import TreeHugger.Generator.TreehuggerGenerator
import TreeHugger.Schema.TypeSchema

object Main extends App{
  //Init the config file to get static params
  val config = ObtainConfigReference("treeHugger") match {
    case Some(value) => value
    case None => throw new RuntimeException("Cannot obtain a reference to the config data.")
  }

  val schema = TypeSchema.fromJson(config.getString("treeHugger.jsonFile"))
  println((new TreehuggerGenerator).generate(schema))

}
