package Nebula.Generator

import HelperUtils.{CreateLogger, ObtainConfigReference}
import Nebula.Schema.{ArgumentSchema, CaseClassSchema}

class CaseClassCodeGenerator
object CaseClassCodeGenerator{


  //Init the config file to get static params
  val config = ObtainConfigReference("nebula") match {
    case Some(value) => value
    case None => throw new RuntimeException("Cannot obtain a reference to the config data.")
  }
  //Init the logger
  val logger = CreateLogger(classOf[CaseClassCodeGenerator])

  def generateCaseClass(caseClass : CaseClassSchema): String = {
    if(caseClass.caseClassArgs.isEmpty) s"case object ${caseClass.caseClassName}"
    else s"case class ${caseClass.caseClassName}${recursivelyGenerateArgs(caseClass.caseClassArgs, 0, "")}"
  }

  //This function recursively generates the Case Class arguments
  def recursivelyGenerateArgs(jsonList: Seq[ArgumentSchema], iterator: Int, arguments: String): String =
    if (iterator >= jsonList.size) arguments
    else if(jsonList.size == 1)   recursivelyGenerateArgs(jsonList, iterator + 1, arguments ++ s"(${jsonList(iterator).argName} : ${jsonList(iterator).argType})")
    else if (iterator == jsonList.size - 1)
      recursivelyGenerateArgs(jsonList, iterator + 1, arguments ++ s" ${jsonList(iterator).argName} : ${jsonList(iterator).argType})")
    else if (iterator == 0)
      recursivelyGenerateArgs(jsonList, iterator + 1, arguments ++ s"(${jsonList(iterator).argName} : ${jsonList(iterator).argType},")
    else
      recursivelyGenerateArgs(jsonList, iterator + 1, arguments ++ s" ${jsonList(iterator).argName} : ${jsonList(iterator).argType},")
}

