package Generator

import NebulaScala2.Scala2Main
import NebulaScala3.Schema.{ActorSchema, ArgumentSchema, CaseSchema, MethodSchema}
import com.typesafe.scalalogging.Logger

class ActorCodeGeneratorOrchestration

object ActorCodeGeneratorOrchestration{
  //Logger init
  val logger = Logger("ActorCodeGenerator")

  def generateActorCode(actorSchemaList : Array[ActorSchema],iterator : Int, actorCodeList : Seq[String]) : Seq[String] =
    if(iterator >= actorSchemaList.length) actorCodeList
    else {
      generateActorCode(
        actorSchemaList, iterator + 1, actorCodeList :+ generateSingleActorCode(actorSchemaList(iterator))
      )
    }

  //This function generates the code of a single Actor
  private def generateSingleActorCode(actorSchema: ActorSchema): String =
    logger.info("[actorCodeGenerator]: Generating the actor code ...")
    val actorCode = generateActor(actorSchema)
    //println(actorCode)
    logger.info("[actorCodeGenerator]: Generating the actor code has ended ...")
    actorCode

  //This function generates the Actor class signature
  private def generateActor(actor: ActorSchema): String =
    s"""import NebulaScala3.message.ProtoMessage
        import NebulaScala2.Scala2Main.generatedActorsRef
        import akka.actor._
        import scala.collection.parallel.CollectionConverters._
    class ${actor.actorName}${recursivelyGenerateArgs(actor.actorArgs, 0, "")} extends Actor with ActorLogging {
    ${recursivelyGenerateMethods(actor.methods, 0, "")}
    ${getActorReferences()}
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
           |log.info("I have received protobuf with name " + protoMessage.name)
           |protoMessage.name match {
           |${generateCaseSchema(jsonList(iterator).caseList, 0, "")}
           |}
           |}
           |}
           |""".stripMargin)
    else recursivelyGenerateMethods(jsonList, iterator + 1, methods)

  /***This function generates a function called getActorRef inside the Actor
      it is used to retrieve the actorReferences for transitions to the next state
   ***/
  private def getActorReferences(): String = {
    """
      |def getActorRef(transitions : Seq[String], iterator: Int, actorRefList: Seq[ActorRef]) : Seq[ActorRef] = {
      |    if (iterator >= transitions.size) actorRefList
      |    else {
      |      println("REFERENCE " + transitions(iterator).toLowerCase)
      |      generatedActorsRef.get(transitions(iterator).toLowerCase) match {
      |        case Some(actorRef) =>
      |          println("REFERENCE " + actorRef)
      |          getActorRef(transitions, iterator + 1, actorRefList :+ actorRef)
      |        case None =>
      |          println("NONE ")
      |          getActorRef(transitions, iterator + 1, actorRefList)
      |      }
      |    }
      | }
      |""".stripMargin
  }

  //This function recursively generates the case inside the method receive of the Actor
  private def generateCaseSchema(caseList: Seq[CaseSchema], iterator: Int, schema: String): String =
    if (iterator >= caseList.size) schema
    else generateCaseSchema(caseList, iterator + 1,
      schema ++
        s"""
           |case "${caseList(iterator).className.toLowerCase}" => {
           |val result = ${caseList(iterator).executionCode}
           |${generateForwardingActors(caseList(iterator).transitions)}
           |}""".stripMargin
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

  //This function generates a list of references and sends the result of the executionCode of the actor to all the transitions
  private def generateForwardingActors(transitionsList: Seq[String]) : String = {
      s"""
        |val actorReferences : Seq[ActorRef] =  getActorRef(
        |Seq(${generateTransitionsList(transitionsList, 0, "")}), 0, Seq.empty
        |)
        |actorReferences.par.foreach(actor => actor ! result)
        |""".stripMargin
    }

  //This function generates a list of transitions to retrieve the ActorReferences associated
  private def generateTransitionsList(transitionsList: Seq[String], iterator : Int, transitionsString: String) : String = {
    if(iterator >= transitionsList.size) transitionsString
    else {
      if(transitionsString == "") {
        generateTransitionsList(
          transitionsList, iterator + 1, transitionsString ++ s"\"${transitionsList(iterator).toLowerCase}\""
        )
      } else {
        generateTransitionsList(
          transitionsList, iterator + 1, transitionsString ++ s", \"${transitionsList(iterator).toLowerCase}\""
        )
      }
    }
  }


}
