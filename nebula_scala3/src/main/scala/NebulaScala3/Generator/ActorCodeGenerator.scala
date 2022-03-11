package NebulaScala3.Generator

import NebulaScala3.Schema.{ActorSchema, ArgumentSchema, CaseSchema, MethodSchema}
import com.typesafe.scalalogging.Logger
class ActorCodeGenerator

object ActorCodeGenerator:
  //Logger init
  val logger = Logger("ActorCodeGenerator")

  def generateActorCode(actorSchemaList : Array[ActorSchema],iterator : Int, actorCodeList : Seq[String]) : Seq[String] = 
    if(iterator >= actorSchemaList.length) actorCodeList
    else {
      generateActorCode(
        actorSchemaList, iterator + 1, actorCodeList :+ generateSingleActorCode(actorSchemaList(iterator))
      )
    }

  private def generateSingleActorCode(actorSchema: ActorSchema): String =
    logger.info("[actorCodeGenerator]: Generating the actor code ...")
    val actorCode = generateActor(actorSchema)
    //println(actorCode)
    logger.info("[actorCodeGenerator]: Generating the actor code has ended ...")
    actorCode

  //This function generates the Actor class signature
  private def generateActor(actor: ActorSchema): String =
    s"""import akka.actor._
        import NebulaScala3.message.ProtoMessage
    class ${actor.actorName}${recursivelyGenerateArgs(actor.actorArgs, 0, "")} extends Actor {
    ${recursivelyGenerateMethods(actor.methods, 0, "")}
    }
    ${generateProps(actor.actorName)}
    ${generateReturnStatement(actor.actorName)}""".stripMargin

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

  //This function recursively generates the Actor methods
  private def recursivelyGenerateMethods(jsonList: Seq[MethodSchema], iterator: Int, methods: String): String =
    if (iterator >= jsonList.size) methods
    else if (jsonList(iterator).methodName == "receive")
      recursivelyGenerateMethods(jsonList, iterator + 1, methods ++
        s"""override def receive: Receive = {
           |case protoMessage : ProtoMessage => {
           |println("I have received protobuf with name " + protoMessage.name)
           |protoMessage.name match {
           |${generateCaseSchema(jsonList(iterator).caseList, 0, "")}
           |}
           |}
           |}
           |""".stripMargin)
    else recursivelyGenerateMethods(jsonList, iterator + 1, methods)

  //This function recursively generates the case inside the method receive of the Actor
  private def generateCaseSchema(caseList: Seq[CaseSchema], iterator: Int, schema: String): String =
    if (iterator >= caseList.size) schema
    else generateCaseSchema(caseList, iterator + 1,
      schema ++
        s"""
           |case "${caseList(iterator).className}" => ${caseList(iterator).executionCode}""".stripMargin
    )

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


