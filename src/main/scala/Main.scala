import HelperUtils.{ObtainConfigReference}
import NebulaScala2.Scala2Main
import NebulaScala3.Scala3Main
import NebulaScala2.Compiler.ActorCodeCompiler
import NebulaScala3.Parser.JSONParser
import NebulaScala3.Generator.ActorCodeGenerator
import NebulaScala2.Compiler.ToolboxGenerator
import org.slf4j.LoggerFactory
import NebulaScala2.ActorTest

class Main

object Main:
  //Init the config file to get static params
  val config = ObtainConfigReference("nebula") match {
    case Some(value) => value
    case None => throw new RuntimeException("Cannot obtain a reference to the config data.")
  }
  @main def hello: Unit =
    println(Scala2Main.scala2Message)
    println(Scala3Main.scala3Message)
    //Get the current Toolbox from the Scala2 APIs
    val toolbox = ToolboxGenerator.generateToolbox()
    //Generate Actors from the JSON schema
    val actorsJson = JSONParser.getActorSchemaFromJson(config.getString("nebula.actorsJsonFile"))
    //val actorCode = ActorCodeGenerator.generateActorCode(actorsJson(0))

    //ActorCodeCompiler.runCode(actorCode, toolbox)
    ActorTest.runActor()







