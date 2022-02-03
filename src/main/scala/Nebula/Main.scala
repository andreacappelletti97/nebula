package Nebula

import HelperUtils.{CreateLogger, ObtainConfigReference}
import Nebula.Compiler.{ActorCompiler, CaseClassCompiler}
import Nebula.Generator.{ActorCodeGenerator, ActorGenerator, ActorSystemGenerator, CaseClassCodeGenerator, CaseClassGenerator}
import Nebula.Parser.JSONParser
import akka.actor.{ActorSystem, Props}

import scala.reflect.runtime.universe
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

  var xxx: Int = _
  case class Authentication(email:String)

  //Init the logger
  val logger = CreateLogger(classOf[Main])

  //Define the current Toolbox
  //val toolbox = currentMirror.mkToolBox()
  val toolbox = universe.runtimeMirror(getClass.getClassLoader).mkToolBox()
  //Init of the caseClassesList sequence
  val caseClassesList = Seq.empty[Symbol]
  val actorSystemEmptyList = Seq.empty[ActorSystem]
  val actorPropsEmptyList: Seq[Props] = Seq.empty[Props]

  //Generate the Actor System from JSON schema
  val actorSystemJson = JSONParser.getActorSystemFromJson(config.getString("nebula.actorSystemJsonFile"))
  //Generate all the case classes definition from the JSON schema
  val caseClassesJson = JSONParser.getCaseClassSchemaFromJson(config.getString("nebula.caseClassesJsonFile"))
  //Generate Actors from the JSON schema
  val actorsJson = JSONParser.getActorSchemaFromJson(config.getString("nebula.actorsJsonFile"))


  //generateActors()

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

  def generateCaseClasses() = {
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
          case  Nebula.Main.Authentication(param) => Nebula.Main.xxx = 10; println("case object instance has been received! " + param)
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
      helloActor ! Authentication("myEmail")
      //Terminate the Actor System
      actorSystem.terminate()
    }
  }


  def generateActors() = {
  actorsJson.foreach(actor => {
    val generatedCode : String = ActorCodeGenerator.generateActorCode(actor)
    //Compile and run the code for each Actor
    if(config.getBoolean("nebula.runCode")) {
      val compiledCode = ActorCompiler.compileCode(generatedCode)
      ActorCompiler.runCode(compiledCode, None)
    }
  })
  }

  Thread.sleep(3000)
  println(s"xxx = ${Nebula.Main.xxx}")

}

