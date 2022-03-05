package NebulaScala3.Generator

import NebulaScala3.Schema.{ArgumentSchema, MessageSchema}

class MessageCodeGenerator

object MessageCodeGenerator:

  def generateCaseClass(caseClass : MessageSchema): String = {
    if(caseClass.messageArgs.isEmpty) s"case object ${caseClass.messageName}"
    else s"case class ${caseClass.messageName}${recursivelyGenerateArgs(caseClass.messageArgs, 0, "")}"
  }

  //This function recursively generates the Case Class arguments
  private def recursivelyGenerateArgs(jsonList: Seq[ArgumentSchema], iterator: Int, arguments: String): String =
    if (iterator >= jsonList.size) arguments
    else if(jsonList.size == 1)   recursivelyGenerateArgs(jsonList, iterator + 1, arguments ++ s"(${jsonList(iterator).argName} : ${jsonList(iterator).argType})")
    else if (iterator == jsonList.size - 1)
      recursivelyGenerateArgs(jsonList, iterator + 1, arguments ++ s" ${jsonList(iterator).argName} : ${jsonList(iterator).argType})")
    else if (iterator == 0)
      recursivelyGenerateArgs(jsonList, iterator + 1, arguments ++ s"(${jsonList(iterator).argName} : ${jsonList(iterator).argType},")
    else
      recursivelyGenerateArgs(jsonList, iterator + 1, arguments ++ s" ${jsonList(iterator).argName} : ${jsonList(iterator).argType},")

  //private def generateReturnStatement() : String =



