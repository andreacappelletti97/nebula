
import HelperUtils.{CreateLogger, ObtainConfigReference}
import Nebula.Generator.ActorCodeGenerator
import Nebula.Parser.JSONParser
import Nebula.Schema.ActorSchema
import NebulaScala2.Compiler.{ActorCompiler, ToolboxGenerator}

class Main

object Main:
  @main def nebulaMain: Unit =
    //Define the current Toolbox
    val toolbox = ToolboxGenerator.generateToolbox()
    //Generate the Actor System from JSON schema
    val actorSystemJson = JSONParser.getActorSystemFromJson("src/main/scala/Nebula/Resources/ActorSystem.json")
    //Generate all the case classes definition from the JSON schema
    val caseClassesJson = JSONParser.getCaseClassSchemaFromJson("src/main/scala/Nebula/Resources/CaseClasses.json")
    //Generate Actors from the JSON schema
    val actorsJson = JSONParser.getActorSchemaFromJson("src/main/scala/Nebula/Resources/Actors.json")

    actorsJson.foreach(actor => {
      val generatedCode : String = ActorCodeGenerator.generateActorCode(actor)
      //Compile and run the code for each Actor
      val compiledCode = ActorCompiler.runCode(generatedCode, toolbox)
    })






