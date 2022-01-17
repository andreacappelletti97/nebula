package Nebula

import HelperUtils.{CreateLogger, ObtainConfigReference}
import Nebula.Compiler.ActorCompiler
import Nebula.Generator.ActorGenerator
import Nebula.Schema.{ActorSchema, ActorSystemSchema, CaseClassSchema}

class Main

object Main extends  App{
  //Init the config file to get static params
  val config = ObtainConfigReference("nebula") match {
    case Some(value) => value
    case None => throw new RuntimeException("Cannot obtain a reference to the config data.")
  }

  //Init the logger
  val logger = CreateLogger(classOf[Main])

  val actorSystemJson = ActorSystemSchema.fromJson(config.getString("nebula.actorSystemJsonFile"))

  val caseClassesJson = CaseClassSchema.fromJson(config.getString("nebula.caseClassesJsonFile"))

  //Generate Actors from the JSON schema
  val actorsJson = ActorSchema.fromJson(config.getString("nebula.actorsJsonFile"))
  actorsJson.foreach(actor => {
    val generatedCode : String = ActorGenerator.generateActor(actor)
    println(generatedCode)
    //Compile and run the code for each Actor
    if(config.getBoolean("nebula.runCode")) {
      val compiledCode = ActorCompiler.compileCode(generatedCode)
      ActorCompiler.runCode(compiledCode)
    }
  })

}
