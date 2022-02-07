package NebulaScala3.Generator

import NebulaScala3.Schema.{ActorSchema, ArgumentSchema, CaseSchema, MethodSchema}

class ActorCodeGenerator

object ActorCodeGenerator:
  
  def generateActorCode(actorSchema: ActorSchema): String =
    println("[actorCodeGenerator]: Generating the actor code ...")
    val actorCode = generateActor(actorSchema)
    println(actorCode)
    println("[actorCodeGenerator]: Generating the actor code has ended ...")
    actorCode

  //This function generates the Actor class signature
  def generateActor(actor: ActorSchema): String =
    s"import akka.actor._" +
      s"\nclass ${actor.actorName}${recursivelyGenerateArgs(actor.actorArgs, 0, "")} extends Actor {" +
      s"\n${recursivelyGenerateMethods(actor.methods, 0, "")}" +
      s"\n}" +
      s"${generateProps(actor.actorName)}" +
      s"${generateReturnStatement(actor.actorName)}"

  //This function recursively generates the Actor arguments
  def recursivelyGenerateArgs(jsonList: Seq[ArgumentSchema], iterator: Int, arguments: String): String =
    if (iterator >= jsonList.size) arguments
    else if (iterator == jsonList.size - 1)
      recursivelyGenerateArgs(jsonList, iterator + 1, arguments ++ s" ${jsonList(iterator).argName} : ${jsonList(iterator).argType})")
    else if (iterator == 0)
      recursivelyGenerateArgs(jsonList, iterator + 1, arguments ++ s"(${jsonList(iterator).argName} : ${jsonList(iterator).argType},")
    else
      recursivelyGenerateArgs(jsonList, iterator + 1, arguments ++ s" ${jsonList(iterator).argName} : ${jsonList(iterator).argType},")

  //This function recursively generates the Actor methods
  def recursivelyGenerateMethods(jsonList: Seq[MethodSchema], iterator: Int, methods: String): String =
    if (iterator >= jsonList.size) methods
    else if (jsonList(iterator).methodName == "receive")
      recursivelyGenerateMethods(jsonList, iterator + 1, methods ++ s"override def receive: Receive = { ${generateCaseSchema(jsonList(iterator).caseList, 0, "")} \n}")
    else recursivelyGenerateMethods(jsonList, iterator + 1, methods)

  //This function recursively generates the case inside the method receive of the Actor
  def generateCaseSchema(caseList: Seq[CaseSchema], iterator: Int, schema: String): String =
    if (iterator >= caseList.size) schema
    else generateCaseSchema(caseList, iterator + 1,
      schema ++ s"\ncase ${caseList(iterator).className} => ${caseList(iterator).executionCode}"
    )

  //This function recursively generates the Actor Props and companion object
  def generateProps(actorName: String): String =
    s"\nobject $actorName {" +
      s"\ndef props() : Props = Props(new $actorName())" +
      s"\n }"

  //This function  generates the Actor return statement for the compilation unit
  def generateReturnStatement(actorName: String) = s"\nreturn $actorName.props()"


