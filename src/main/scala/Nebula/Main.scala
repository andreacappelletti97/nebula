package Nebula

import HelperUtils.ObtainConfigReference
import NebulaScala2.Compiler.{ActorCodeCompiler, DynamicActorCodeCompiler, MessageCodeCompiler, ToolboxGenerator}
import NebulaScala2.{Compiler, Scala2Main}
import NebulaScala3.Generator.{ActorCodeGenerator, DynamicActorGenerator, DynamicMessageGenerator, MessageCodeGenerator, ProtoMessageGenerator}
import NebulaScala3.Parser.{JSONParser, YAMLParser}
import NebulaScala3.Scala3Main
import com.typesafe.scalalogging.Logger
import NebulaScala2.Scala2Main.generatedActorsProps
import NebulaScala3.Scala3Main.protoBufferList

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

    //TODO: add yaml configuration
    //Generate Actors from the JSON schema
    val actorsJson = if(config.getBoolean("nebula.buildArtifact")) JSONParser.getActorSchemaFromJson(config.getString("nebula.actorsBuildJsonFile"))  else  JSONParser.getActorSchemaFromJson(config.getString("nebula.actorsJsonFile"))
    val messagesJson = if(config.getBoolean("nebula.buildArtifact")) JSONParser.getMessagesSchemaFromJson(config.getString("nebula.messagesBuildJsonFile")) else JSONParser.getMessagesSchemaFromJson(config.getString("nebula.messagesJsonFile"))
    //Dynamic
    val dynamicActorJson = if(config.getBoolean("nebula.buildArtifact")) JSONParser.getDynamicActorsFromJson(config.getString("nebula.actorsDynamicJsonFile")) else JSONParser.getDynamicActorsFromJson(config.getString("nebula.actorsDynamicJsonFile"))
    //val dynamicMessageJson = if(config.getBoolean("nebula.buildArtifact")) JSONParser.getDynamicMessagesFromJson(config.getString("nebula.messagesDynamicJsonFile")) else JSONParser.getDynamicMessagesFromJson(config.getString("nebula.messagesDynamicJsonFile"))

    //Store dynamic messages constructs to keep track of their arguments nature
    //dynamicMessages = dynamicMessageJson
    //Store dynamic messages builders --> used by Nebula to orchestrate
    //dynamicMessagesBuilders = DynamicMessageGenerator.generateDynamicMessages(dynamicMessageJson, 0, Seq.empty)
    //println(dynamicMessagesBuilders)

    //Generate Dynamic Actor Code
    //val dynamicActor = DynamicActorGenerator.generateActorCode(dynamicActorJson, 0, Seq.empty)
    //Store the generated actor props into a global state for orchestration

    val actorCode = ActorCodeGenerator.generateActorCode(actorsJson, 0, Seq.empty)

    println(actorCode)
    logger.info("Actors code have been generated...")

    val generatedActors = ActorCodeCompiler.compileActors(actorCode, toolbox, 0, Seq.empty)
    generatedActors.foreach(actor =>
      generatedActorsProps += "actorName" -> (actor)
    )

    println(generatedActorsProps)

    logger.info("Actor Props have been generated...")

    //Generate messages within the standard ProtoBuffer
    val protoMessages = ProtoMessageGenerator.generateProtoMessages(
      messagesJson,
      0,
      Seq.empty
    )
    protoMessages.foreach(message => protoBufferList +=  message.name -> message)

    println(protoBufferList)

    logger.info("ProtoMessages have been generated...")

    val firstMessage = protoBufferList.get("Init")
    println(firstMessage)


/*    val proto = ProtoMessage("ciao", Map("AL" -> "Alabama", "AK" -> "Alaska"))
    println(proto.content)
    println(proto.args)*/

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









