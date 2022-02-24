package Nebula

import HelperUtils.ObtainConfigReference
import NebulaScala2.Compiler.{ActorCodeCompiler, MessageCodeCompiler, ToolboxGenerator}
import NebulaScala2.{Compiler, Scala2Main}
import NebulaScala3.Generator.{ActorCodeGenerator, MessageCodeGenerator}
import NebulaScala3.Parser.{JSONParser, YAMLParser}
import NebulaScala3.Scala3Main
import com.typesafe.scalalogging.Logger

class Main

object Main:
  //Init the config file to get static params
  val config = ObtainConfigReference("nebula") match {
    case Some(value) => value
    case None => throw new RuntimeException("Cannot obtain a reference to the config data.")
  }
  //Logger init
  val logger = Logger("Main")
  //Main method of the framework
  @main def nebulaMain: Unit =
    logger.info(Scala2Main.scala2Message)
    logger.info(Scala3Main.scala3Message)
    //Init Kamon monitoring instrumentation
    if(config.getBoolean("nebula.enableKamon")) Scala2Main.initKamon()
    //Get the current Toolbox from the Scala2 APIs
    val toolbox = ToolboxGenerator.generateToolbox()
    //Generate Actors from the JSON schema
    val actorsJson = if(config.getBoolean("nebula.buildArtifact")) JSONParser.getActorSchemaFromJson(config.getString("nebula.actorsBuildJsonFile"))  else  JSONParser.getActorSchemaFromJson(config.getString("nebula.actorsJsonFile"))
    val messagesJson = if(config.getBoolean("nebula.buildArtifact")) JSONParser.getMessagesSchemaFromJson(config.getString("nebula.messagesBuildJsonFile")) else JSONParser.getMessagesSchemaFromJson(config.getString("nebula.messagesJsonFile"))
    val dynamicMessageJson = if(config.getBoolean("nebula.buildArtifact")) JSONParser.getDynamicMessagesFromJson(config.getString("nebula.dynamicMessagesBuildJsonFile")) else JSONParser.getDynamicMessagesFromJson(config.getString("nebula.dynamicMessagesJsonFile"))
    
    /*
    toolbox.eval(
      toolbox.parse(
      """case class Film(title: String) extends NebulaScala3.Scala3Main.SomeTrait;
          |NebulaScala3.Scala3Main.theKeeper = Film("Kingdom")""".stripMargin)
    )
    println(NebulaScala3.Scala3Main.theKeeper)

    val actorCode = ActorCodeGenerator.generateActorCode(actorsJson, 0, Seq.empty)
    println(actorCode)

    val actorProps = ActorCodeCompiler.compileActors(actorCode, toolbox, 0, Seq.empty)
    println(actorProps)
    
    ActorCodeCompiler.runExample(actorProps(0), toolbox)
    */

    //println(s"xxx = ${Scala3Main.xxx}")
    //Run the code
    //Thread.sleep(3000)
    //println(s"xxx = ${Scala3Main.xxx}")








