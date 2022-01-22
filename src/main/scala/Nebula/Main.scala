package Nebula

import HelperUtils.{CreateLogger, ObtainConfigReference}
import Nebula.Compiler.{ActorCompiler, CaseClassCompiler}
import Nebula.Generator.{ActorGenerator, ActorSystemGenerator, CaseClassGenerator}
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
  val actorSystemEmptyList = Seq.empty[ActorSystem]
  val actorPropsEmptyList: Seq[Props] = Seq.empty[Props]

  //Generate the Actor System from JSON schema
  val actorSystemJson = ActorSystemSchema.fromJson(config.getString("nebula.actorSystemJsonFile"))
  //Generate all the case classes definition from the JSON schema
  val caseClassesJson = CaseClassSchema.fromJson(config.getString("nebula.caseClassesJsonFile"))
  //Generate Actors from the JSON schema
  val actorsJson = ActorSchema.fromJson(config.getString("nebula.actorsJsonFile"))


  generateCaseClasses()

  def generateActorSystem(): Unit = {
    //Get all the Actor Props compilation units
    val actorPropsList = ActorCompiler.getCompiledActors(actorsJson, 0, actorPropsEmptyList)
    println(actorPropsList)
    //Generate all the actorSystems of the configuration and store them into a sequence
    val actorSystemList: Seq[ActorSystem] = ActorSystemGenerator.generateActorSystem(actorSystemJson, 0 , actorSystemEmptyList)
    actorSystemList.zipWithIndex.foreach{ case (actorSystem, index) =>
      val actorInstances = actorSystemJson(index).actorsInstances
      println(actorInstances)
    }

  }

  def generateCaseClasses() {
  caseClassesJson.foreach(caseClassDefinition => {
    //TODO: generate a Map to keep track of the caseClasses indexes
    val generatedCode : String = CaseClassGenerator.generateCaseClass(caseClassDefinition)
    println(generatedCode)
  })
    if(config.getBoolean("nebula.generateCaseClasses")) {
      //Define the case classes into the current Toolbox
      val definedCaseClasses = CaseClassCompiler.defineCode(caseClassesJson, 0, caseClassesList)
      logger.info(s"The defined case classes are $definedCaseClasses")
      val definedCaseObject = definedCaseClasses(0).fullName

      // Actor code that recognise the defined case object
      val actorCode = q"""
      import akka.actor._
      object HelloActor {
        def props() = Props(new HelloActor())
      }
      class HelloActor() extends Actor {
        def receive = {
          case $definedCaseObject  => println("case object instance has been received!")
          case _       => println("None received!")
        }
      }
      return HelloActor.props()
      """
      //Compile the Actor code
      val compiledCode = toolbox.compile(actorCode)()
      val actorProps = compiledCode.asInstanceOf[Props]
      //Create a test for the current Actor with a dummy actorSystem
      val actorSystem = ActorSystem("system")
      val helloActor = actorSystem.actorOf(actorProps)
      //Send the case object message to the Actor
      helloActor ! definedCaseObject

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
