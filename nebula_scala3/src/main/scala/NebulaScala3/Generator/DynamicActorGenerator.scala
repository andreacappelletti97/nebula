package NebulaScala3.Generator

import NebulaScala3.Schema.{ActorDynamicSchema, ArgumentSchema, BehaviorSchema, MethodSchema}
import com.typesafe.scalalogging.Logger

class DynamicActorGenerator

object DynamicActorGenerator {
  //Logger init
  val logger = Logger("DynamicActorGenerator")

  def generateActorCode(actorSchemaList : Array[ActorDynamicSchema], iterator : Int, actorCodeList : Seq[String]) : Seq[String] = {
    if (iterator >= actorSchemaList.length) actorCodeList
    else {
      generateActorCode(
        actorSchemaList, iterator + 1, actorCodeList :+ generateSingleActorCode(actorSchemaList(iterator))
      )
    }
  }

    private def generateSingleActorCode(actorSchema: ActorDynamicSchema): String = {
      logger.info ("[actorCodeGenerator]: Generating the actor code ...")
      val actorCode = generateActor (actorSchema)
      //println(actorCode)
      logger.info ("[actorCodeGenerator]: Generating the actor code has ended ...")
      actorCode
    }

    //This function generates the dynamic Actor class signature
    private def generateActor(actor: ActorDynamicSchema): String = {
      s"""import akka.actor._
      class ${actor.actorName}${recursivelyGenerateArgs (actor.actorArgs, 0, "")} extends Actor {
      override def receive: Receive = {
      case dynamicMessage: DynamicMessage => {
      dynamicMessage.getDescriptorForType.getName match {
      ${recursivelyGenerateActorBehavior(actor.behavior, 0, "")}
      }
      }
      }
      }
      ${generateProps (actor.actorName)}
      ${generateReturnStatement (actor.actorName)}""".stripMargin
    }

  //This function recursively generates the Actor behavior
  private def recursivelyGenerateActorBehavior(actorBehaviorList : Seq[BehaviorSchema], iterator: Int, actorBehavior : String) : String = {
    if(iterator >= actorBehaviorList.size) actorBehavior
    else recursivelyGenerateActorBehavior(actorBehaviorList, iterator + 1, actorBehavior +
      s"""
        |case ${actorBehaviorList(iterator).input} => {
        |println(dynamicMessage.getDescriptorForType.getName)
        |}
        |""".stripMargin
    )

  }


  //This function recursively generates the Actor arguments
  private def recursivelyGenerateArgs(jsonList: Seq[ArgumentSchema], iterator: Int, arguments: String): String =
    if (iterator >= jsonList.size) arguments
    else if(jsonList.size == 1) recursivelyGenerateArgs(jsonList, iterator + 1, arguments ++ s"(${jsonList(iterator).argName} : ${jsonList(iterator).argType})")
    else if (iterator == jsonList.size - 1)
      recursivelyGenerateArgs(jsonList, iterator + 1, arguments ++ s" ${jsonList(iterator).argName} : ${jsonList(iterator).argType})")
    else if (iterator == 0)
      recursivelyGenerateArgs(jsonList, iterator + 1, arguments ++ s"(${jsonList(iterator).argName} : ${jsonList(iterator).argType},")
    else
      recursivelyGenerateArgs(jsonList, iterator + 1, arguments ++ s" ${jsonList(iterator).argName} : ${jsonList(iterator).argType},")

  //This function recursively generates the Actor Props and companion object
  private def generateProps(actorName: String): String =
    s"""
       |object $actorName {
       |def props() : Props = Props(new $actorName())
       |}
       |""".stripMargin

  //This function  generates the Actor return statement for the compilation unit
  private def generateReturnStatement(actorName: String) =
    s"""
       |return $actorName.props()""".stripMargin

}
