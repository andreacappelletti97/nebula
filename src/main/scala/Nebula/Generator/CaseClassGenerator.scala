package Nebula.Generator

import Nebula.Schema.CaseClassSchema
import treehugger.forest._
import definitions._
import treehuggerDSL._


object CaseClassGenerator {
  object sym {
    val Expr = RootClass.newClass("Expr")
    val Var  = RootClass.newClass("Var")
  }

  def generateCaseClass(jsonSchema: CaseClassSchema): String = {
    if(jsonSchema.caseClassArgs.isEmpty){
      //Generate case object definition
      val tree = (CASEOBJECTDEF(jsonSchema.caseClassName): Tree)
      treeToString(tree)
    } else {
      //Generate list of case class arguments
      val caseClassArguments = jsonSchema.caseClassArgs.map { arg =>
        val argName = arg.argName
        val argType = arg.argType
        PARAM(argName, argType): ValDef
      }
      //Generate case class definition
      val tree = (CASECLASSDEF(jsonSchema.caseClassName)
        withParams (caseClassArguments): Tree)
      treeToString(tree)
    }

  }
}
