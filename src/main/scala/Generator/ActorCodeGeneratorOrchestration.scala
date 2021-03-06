package Generator

import NebulaScala2.Scala2Main
import NebulaScala3.Schema.{ActorSchema, ArgumentSchema, CaseSchema, CustomObjectSchema, ExternalEndpointSchema, ExternalJarSchema, MethodSchema, VariableSchema}
import com.typesafe.scalalogging.Logger

import scala.annotation.tailrec

class ActorCodeGeneratorOrchestration

object ActorCodeGeneratorOrchestration {
  //Logger init
  val logger = Logger("ActorCodeGenerator")

  def generateActorCode(actorSchemaList: Array[ActorSchema], iterator: Int, actorCodeList: Seq[String]): Seq[String] =
    if (iterator >= actorSchemaList.length) actorCodeList
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
        import java.net.URL
        import com.lightbend.cinnamon.akka.CinnamonMetrics
        import com.lightbend.cinnamon.event.Event
        import com.lightbend.cinnamon.metric.Counter
        import com.lightbend.cinnamon.metric.Recorder
        import com.lightbend.cinnamon.metric.GaugeDouble
        import com.lightbend.cinnamon.metric.GaugeLong
        import com.lightbend.cinnamon.metric.Rate
        import java.util
        import java.util.HashMap
        import Jars.LoadExternalJar.child
        import java.net.URLClassLoader
        import scala.collection.parallel.CollectionConverters._
    class ${actor.actorName}${recursivelyGenerateArgs(actor.actorArgs, 0, "")} extends Actor with ActorLogging {
    val mailBoxSize: Counter = CinnamonMetrics(context).createCounter("mailBoxSize")
    val stashSize: Counter = CinnamonMetrics(context).createCounter("stashSize")
    val processingTime: Recorder = CinnamonMetrics(context).createRecorder("processingTime")
    val mailboxTime: Recorder = CinnamonMetrics(context).createRecorder("mailboxTime")
    ${recursivelyGenerateVariables(actor.actorVars, 0, "")}
    ${recursivelyGenerateMethods(actor.methods, 0, "")}
    $getActorReferences
    }
    ${generateProps(actor.actorName)}
    ${generateReturnStatement(actor.actorName)}""".stripMargin

  //This function recursively generates the Actor arguments
  @tailrec
  private def recursivelyGenerateArgs(jsonList: Seq[ArgumentSchema], iterator: Int, arguments: String): String =
    if (iterator >= jsonList.size) arguments
    else if(jsonList.size == 1) recursivelyGenerateArgs(jsonList, iterator + 1, arguments ++ s"(${jsonList(iterator).argName} : ${jsonList(iterator).argType})")
    else if (iterator == jsonList.size - 1)
      recursivelyGenerateArgs(jsonList, iterator + 1, arguments ++ s" ${jsonList(iterator).argName} : ${jsonList(iterator).argType})")
    else if (iterator == 0)
      recursivelyGenerateArgs(jsonList, iterator + 1, arguments ++ s"(${jsonList(iterator).argName} : ${jsonList(iterator).argType},")
    else
      recursivelyGenerateArgs(jsonList, iterator + 1, arguments ++ s" ${jsonList(iterator).argName} : ${jsonList(iterator).argType},")


  private def recursivelyGenerateVariables(
                                            jsonList: Seq[VariableSchema],
                                            iterator: Int,
                                            variables: String
                                          ): String =
    if(jsonList != null) {
      if (iterator >= jsonList.size) variables
      else
        recursivelyGenerateVariables(
          jsonList,
          iterator + 1,
          variables ++ s" var ${jsonList(iterator).variableName} : ${
            jsonList(
              iterator
            ).variableType
          } = ${jsonList(iterator).variableValue}"
        )
    } else variables

  //This function recursively generates the Actor methods
  @tailrec
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
           |${generateCustomSchema(jsonList(iterator).customObject, 0, "")}
           |}
           |""".stripMargin)
    else recursivelyGenerateMethods(jsonList, iterator + 1, methods)

  //This function recursively generates the case inside the method receive of the Actor
  private def generateCustomSchema(caseList: Seq[CustomObjectSchema], iterator: Int, schema: String): String =
    if(caseList != null) {
      if (iterator >= caseList.size) schema
      else generateCustomSchema(caseList, iterator + 1,
        schema ++
          s"""
             |case ${caseList(iterator).className.toLowerCase} => {
             |${generateCustomExecutionCode(caseList(iterator))}

             |${generateForwardingActors(caseList(iterator).transitions)}

             |}""".
            stripMargin
    )
      } else schema



  /** *This function generates a function called getActorRef inside the Actor
   * it is used to retrieve the actorReferences for transitions to the next state
   * * */
  private def getActorReferences: String = {
    """
      |def getActorRef(transitions : Seq[String], iterator: Int, actorRefList: Seq[ActorRef]) : Seq[ActorRef] = {
      | log.info("Entering getActorRef...")
      |    var actorRefList: Seq[ActorRef] = Seq()
      |    var iterator = 0
      |    while(iterator < transitions.size){
      |       generatedActorsRef.get(transitions(iterator).toLowerCase) match {
      |        case Some(actorRef) => {
      |           actorRefList = actorRefList ++ actorRef }
      |         case None => { log.info("[getActorRef]: ActorRef not found for " + transitions(iterator)) }
      |      }
      |      iterator = iterator + 1
      |    }
      |    log.info("[getActorRef]: ActorRef list: " + actorRefList)
      |    actorRefList
      | }
      |""".stripMargin
  }

  //This function recursively generates the case inside the method receive of the Actor
  @tailrec
  private def generateCaseSchema(caseList: Seq[CaseSchema], iterator: Int, schema: String): String =
    if(caseList != null){
    if (iterator >= caseList.size) schema
    else generateCaseSchema(caseList, iterator + 1,
      schema ++
        s"""
           |case "${caseList(iterator).className.toLowerCase}" => {
           |${generateExecutionCode(caseList(iterator))}
           |${generateForwardingActors(caseList(iterator).transitions)}
           |}""".stripMargin
    )} else schema

  // Generate Scala execution code or execute an external jar
  private def generateCustomExecutionCode(executionCode: CustomObjectSchema): String =
    if(executionCode.executionCode != null) {
      s"""${executionCode.executionCode}"""
    }
    else{
      val jar = executionCode.endpointSchema
      s"""|val child = new URLClassLoader(Array(new URL("file:///${jar.url}")), this.getClass.getClassLoader)
          |val classToLoad = Class.forName("${jar.className}", true, child)
          |val param: ${jar.param.argType} = ${jar.param.argValue}
          |val method = classToLoad.getDeclaredMethod("${jar.methodName}", classOf[${jar.methodType}])
          |val result = method.invoke(classToLoad.getDeclaredConstructor().newInstance(), param)
          |log.info("Endpoint returned: " + result)
          |""".stripMargin
    }

  // Generate Scala execution code or execute an external jar
  private def generateExecutionCode(executionCode: CaseSchema): String =
    if(executionCode.executionCode != null) {
      s"""${executionCode.executionCode}"""
    }
    else{
      val jar = executionCode.endpointSchema
      s"""|val child = new URLClassLoader(Array(new URL("file:///${jar.url}")), this.getClass.getClassLoader)
          |val classToLoad = Class.forName("${jar.className}", true, child)
          |val param: ${jar.param.argType} = ${jar.param.argValue}
          |val method = classToLoad.getDeclaredMethod("${jar.methodName}", classOf[${jar.methodType}])
          |val result = method.invoke(classToLoad.getDeclaredConstructor().newInstance(), param)
          |log.info("Endpoint returned: " + result)
          |""".stripMargin
    }

  // Generate Scala execution code for external jar inside the Actor
  private def generateExternalJarCode(jar: ExternalJarSchema): String =
    s"""
       |val child = new URLClassLoader(Array(new URL("file:///${jar.url}")), this.getClass.getClassLoader)
       |val classToLoad = Class.forName("${jar.className}", true, child)
       |val method = classToLoad.getDeclaredMethod("${jar.methodName}", classOf[String])
       |val result = method.invoke(classToLoad.getDeclaredConstructor().newInstance()")
       |""".stripMargin

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
    if(transitionsList != null && transitionsList.nonEmpty) {
      s"""
         |log.info("Generating forwarding actors...")
         |val actorReferences : Seq[ActorRef] =  getActorRef(
         |Seq(${generateTransitionsList(transitionsList, 0, "")}), 0, Seq.empty
         |)
         |if(actorReferences.nonEmpty) {
         |actorReferences.par.foreach(actor => actor ! result)
         |}
         |""".stripMargin
    } else ""
  }

  //This function generates a list of transitions to retrieve the ActorReferences associated
  @tailrec
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