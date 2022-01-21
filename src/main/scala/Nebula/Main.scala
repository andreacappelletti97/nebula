package Nebula

import HelperUtils.{CreateLogger, ObtainConfigReference}
import Nebula.Compiler.{ActorCompiler, CaseClassCompiler}
import Nebula.Generator.{ActorGenerator, CaseClassGenerator}
import Nebula.Schema.{ActorSchema, ActorSystemSchema, CaseClassSchema}
import akka.actor.{ActorSystem, Props}

import scala.reflect.runtime.currentMirror
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
      //Define the case classes into the current Toolbox
      val myClasses = CaseClassCompiler.defineCode(caseClassesJson, 0, caseClassesList)
      println(myClasses)
      val myCaseClass = myClasses(0)
      val authenticationName = myCaseClass.name
      val myTest = myClasses(1)
      println(myTest)
      // Class instance
      val actorCode = q"""
       import akka.actor._
       object HelloActor {
          def props() = Props(new HelloActor())
       }
       class HelloActor() extends Actor {
          def receive = {
            case $authenticationName => println("hello")
            case _       => println("Something received...")
          }
      }
      return HelloActor.props()"""
      //Compile the code
      val compiledCode = toolbox.compile(actorCode)()
      println(compiledCode)
      //Instantiate a dummy actor system to test the compiled code
      val actorSystem = ActorSystem("firstActorSystem")
      val myProps = compiledCode.asInstanceOf[Props]
      val helloActor = actorSystem.actorOf(myProps)
      //Send the case object to the compiled actor
      helloActor ! myCaseClass
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
