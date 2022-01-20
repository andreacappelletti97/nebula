package Nebula

import HelperUtils.{CreateLogger, ObtainConfigReference}
import Nebula.Compiler.{ActorCompiler, CaseClassCompiler}
import Nebula.Generator.{ActorGenerator, CaseClassGenerator}
import Nebula.Schema.{ActorSchema, ActorSystemSchema, CaseClassSchema}

import scala.reflect.runtime.{currentMirror}
import scala.reflect.runtime.universe._
import scala.tools.reflect.ToolBox
import scala.language.implicitConversions

class Main

object Main extends  App{
  //Init the config file to get static params
  val config = ObtainConfigReference("nebula") match {
    case Some(value) => value
    case None => throw new RuntimeException("Cannot obtain a reference to the config data.")
  }

  //Init the logger
  val logger = CreateLogger(classOf[Main])

  //Define the current Toolbox
  val toolbox = currentMirror.mkToolBox()
  //val tb = universe.runtimeMirror(getClass.getClassLoader).mkToolBox()
  //Init of the caseClassesList sequence
  val caseClassesList = Seq.empty[Symbol]

  //Generate the Actor System from JSON schema
  val actorSystemJson = ActorSystemSchema.fromJson(config.getString("nebula.actorSystemJsonFile"))
  //Generate all the case classes definition from the JSON schema
  val caseClassesJson = CaseClassSchema.fromJson(config.getString("nebula.caseClassesJsonFile"))
  //Generate Actors from the JSON schema
  val actorsJson = ActorSchema.fromJson(config.getString("nebula.actorsJsonFile"))


  generateCaseClasses()

  def generateActorSystem(): Unit = {
    actorSystemJson.foreach(actorSystem => {
      //TODO: generate actor system and instantiate the right number of Actors
      println(actorSystem.actorSystemName)
    })
  }

  def generateCaseClasses() {
  caseClassesJson.foreach(caseClassDefinition => {
    //TODO: generate a Map to keep track of the caseClasses indexes
    val generatedCode : String = CaseClassGenerator.generateCaseClass(caseClassDefinition)
    println(generatedCode)
  })
    if(config.getBoolean("nebula.generateCaseClasses")) {
      val myClasses = CaseClassCompiler.defineCode(caseClassesJson, 0, caseClassesList)
      println(myClasses)
      val myCaseClass = myClasses(0)
      val number = q"""new $myCaseClass("hey")"""
      println(toolbox.eval(number))

    }


  }

  def generateActors() {
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
}
