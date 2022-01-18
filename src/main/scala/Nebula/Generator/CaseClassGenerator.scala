package Nebula.Generator

import Nebula.Schema.CaseClassSchema
import treehugger.forest._
import definitions._
import treehuggerDSL._
import treehugger.forest._
import definitions._
import treehuggerDSL._

object CaseClassGenerator {
  object sym {
    val Expr = RootClass.newClass("Expr")
    val Var  = RootClass.newClass("Var")
  }

  def generateCaseClass(jsonSchema: CaseClassSchema): String = {
    //Generate list of case class arguments
    val caseClassArguments = jsonSchema.caseClassArgs.map { arg =>
      val argName = arg.argName
      val argType = arg.argType
      PARAM(argName, argType): ValDef
    }

    //Generate case class definition
    val tree =  (CASECLASSDEF(jsonSchema.caseClassName)
      withParams(caseClassArguments) : Tree)

    treeToString(tree)
  }
}
