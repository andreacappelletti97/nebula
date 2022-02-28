package NebulaScala3.Generator

import NebulaScala3.Schema.ActorDynamicSchema
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
      //val actorCode = generateActor (actorSchema)
      //println(actorCode)
      logger.info ("[actorCodeGenerator]: Generating the actor code has ended ...")
      //actorCode
      ""
    }
    /*
    //This function generates the Actor class signature
    private def generateActor(actor: ActorDynamicSchema): String = {
      s"""import akka.actor._
      class ${actor.actorName}${recursivelyGenerateArgs (actor.actorArgs, 0, "")} extends Actor {
      ${recursivelyGenerateMethods (actor.methods, 0, "")}
      }
      ${generateProps (actor.actorName)}
      ${generateReturnStatement (actor.actorName)}""".stripMargin
    }
*/

}
