import HelperUtils.{CreateLogger, ObtainConfigReference}
import Nebula.Generator.ActorCodeGenerator
import Nebula.Parser.JSONParser
import Nebula.Schema.ActorSchema
import NebulaScala2.Compiler.{ActorCodeCompiler, ToolboxGenerator}

class Main

object Main:
  @main def nebulaMain: Unit =
    //Get the current Toolbox from the Scala2 APIs
    val toolbox = ToolboxGenerator.generateToolbox()
    //Generate the Actor System from JSON schema
    val actorSystemJson = JSONParser.getActorSystemFromJson("src/main/scala/Nebula/Resources/ActorSystem.json")
    //Generate all the case classes definition from the JSON schema
    val caseClassesJson = JSONParser.getCaseClassSchemaFromJson("src/main/scala/Nebula/Resources/CaseClasses.json")
    //Generate Actors from the JSON schema
    val actorsJson = JSONParser.getActorSchemaFromJson("src/main/scala/Nebula/Resources/Actors.json")

    //Compile and run the code for each Actor
    actorsJson.foreach(actor => {
      val generatedCode : String = ActorCodeGenerator.generateActorCode(actor)
      //Use the Scala2 APIs to compile and run the code in the current Toolbox
      val compiledCode = ActorCodeCompiler.runCode(generatedCode, toolbox)
    })






