package Nebula

import Generator.ActorCodeGeneratorOrchestration
import HelperUtils.ObtainConfigReference
import scala.concurrent._
import ExecutionContext.Implicits.global
import NebulaScala2.Compiler.{ActorCodeCompiler, MessageCodeCompiler, ToolboxGenerator}
import NebulaScala2.{Compiler, Scala2Main}
import NebulaScala3.Generator.{ConfigCodeGenerator, ProtoMessageGenerator}
import NebulaScala3.Parser.{JSONParser, YAMLParser}
import NebulaScala3.Scala3Main
import com.typesafe.scalalogging.Logger
import NebulaScala2.Scala2Main.generatedActorsProps
import NebulaScala3.Scala3Main.protoBufferList
import NebulaScala2.Scala2Main.generatedActorsRef
import NebulaScala2.Scala2Main.generatedActorSystems
import NebulaScala3.message.ProtoMessage
import akka.actor.{ActorRef, ActorSystem}
import com.typesafe.config.Config

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.io.StdIn.readLine

class Main

object Main:
  //Init the config file to get static params
  val config: Config = ObtainConfigReference("nebula") match {
    case Some(value) => value
    case None => throw new RuntimeException("Cannot obtain a reference to the config data.")
  }
  //Logger init
  val logger: Logger = Logger("Main")

  def stopNebula(): Unit =
    generatedActorSystems.foreach {
      case (name, actorSystem) =>
        actorSystem.terminate()
        logger.info(s"ActorSystem $name has been terminated...")
    }



  //Main method of the framework
  def startNebula(actorJsonPath: String, messagesJsonPath: String, orchestratorPath: String, clusterShardingConfigPath: String): Unit =
    logger.info(Scala2Main.scala2Message)
    logger.info(Scala3Main.scala3Message)

    //Init Kamon monitoring instrumentation
    if (config.getBoolean("nebula.enableKamon")) Scala2Main.initKamon()

    //Get the current Toolbox from the Scala2 APIs
    val toolbox = ToolboxGenerator.generateToolbox()

    //TODO: add yaml configuration
    val actorsJson = JSONParser.getActorSchemaFromJson(actorJsonPath)
    val messagesJson = JSONParser.getMessagesSchemaFromJson(messagesJsonPath)
    val orchestratorJson = JSONParser.getOrchestratorFromJson(orchestratorPath)
    val clusterShardingJson = JSONParser.getClusterShardingSchemaFromJson(clusterShardingConfigPath)
    val clusterConfigCode = ConfigCodeGenerator.generateClusterConfigCode(clusterShardingJson)

    Thread.sleep(3000)
    //Generate ActorCode as String
    val actorCode = ActorCodeGeneratorOrchestration.generateActorCode(actorsJson, 0, Seq.empty)

    logger.info("Actors code have been generated...")
    println(actorCode)
    Thread.sleep(3000)

    //Generate ActorProps
    val generatedActors = ActorCodeCompiler.compileActors(actorCode, toolbox, 0, Seq.empty)
    logger.info("Actors code have been compiled into Props objects...")

    //Store ActorProps into the global state of Nebula
    generatedActors.zipWithIndex.foreach {
      case (actor, index) =>
        generatedActorsProps += actorsJson(index).actorName.toLowerCase -> actor
    }

    logger.info("Actor Props have been generated...")
    println(generatedActorsProps)
    Thread.sleep(3000)

    //Generate messages within the standard ProtoBuffer
    val protoMessages = ProtoMessageGenerator.generateProtoMessages(
      messagesJson,
      0,
      Seq.empty
    )

    //Store generated ProtoBuffer into the global state of Nebula
    protoMessages.foreach(message => protoBufferList += message.name.toLowerCase -> message)

    logger.info("ProtoMessages have been generated...")
    println(protoBufferList)
    Thread.sleep(3000)

    val actorSystem: ActorSystem = ActorSystemFactory.initActorSystem("system")
    generatedActorSystems += "system" -> actorSystem

    //Start Nebula orchestration --> init actors from actorProps stored
    orchestratorJson.foreach { actor =>
      val actorProps = generatedActorsProps.getOrElse(actor.name.toLowerCase, return)
      val actorRef: ActorRef = ActorFactory.initActor(actorSystem, actorProps, actor.name)
      generatedActorsRef = generatedActorsRef += (actor.name.toLowerCase -> actorRef)
    }

    logger.info("End of ActorRef init ...")
    println(generatedActorsRef)
    Thread.sleep(3000)

    logger.info("Sending init messages...")
    //Send init messages --> send init messages to the Actor instantiated
    orchestratorJson.foreach { orchestration =>
      val actor = generatedActorsRef.getOrElse(orchestration.name.toLowerCase, return)
      orchestration.initMessages.zipWithIndex.foreach {
        case (message, index) =>
        val protoMessage = protoBufferList.getOrElse(message.toLowerCase, return)
        sendMessage(actor, protoMessage, orchestration.timeInterval(index), orchestration.numOfMessages(index))
        actor ! protoMessage
      }
    }

    def sendMessage(actorRef: ActorRef, message: ProtoMessage, timeInterval: Int, numOfMessages: Int): Future[Unit] = Future {
      if(numOfMessages > 0) {
        logger.info("Sending a message...")
        actorRef ! message
        Thread.sleep(1000*timeInterval.toLong)
        sendMessage(actorRef, message, timeInterval, numOfMessages - 1)
      }
    }





