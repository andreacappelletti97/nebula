package Nebula.Compiler

import HelperUtils.{CreateLogger, ObtainConfigReference}
import Nebula.Generator.CaseClassGenerator
import Nebula.Main.toolbox
import Nebula.Schema.CaseClassSchema

import scala.reflect.runtime.universe._

class CaseClassCompiler

object CaseClassCompiler{
  //Init the config file to get static params
  val config = ObtainConfigReference("nebula") match {
    case Some(value) => value
    case None => throw new RuntimeException("Cannot obtain a reference to the config data.")
  }
  //Init the logger
  val logger = CreateLogger(classOf[CaseClassCompiler])



  //Recursively populate the case classes array with defined case classes
  def defineCode(codeToCompile : Array[CaseClassSchema], iterator: Int, caseClassList : Seq[Symbol]): Seq[Symbol] = {
  if(iterator >= codeToCompile.length) caseClassList
  else {
    defineCode(
      codeToCompile, iterator + 1, caseClassList :+ compileCode(
        CaseClassGenerator.generateCaseClass(codeToCompile(iterator))
      )
    )
  }
  }

  //Define the case class inside the current Toolbox and return it
  def compileCode(codeToCompile: String): Symbol = {
    val tree = toolbox.parse(codeToCompile)
    if(codeToCompile.contains("object")){
      val caseObjectDefinition: ModuleDef = tree.asInstanceOf[ModuleDef]
      val definedClass = toolbox.define(caseObjectDefinition)
      println(s"Defined class is $definedClass")
      definedClass
    } else {
      val classDefinition : ClassDef = tree.asInstanceOf[ClassDef]
      val definedClass = toolbox.define(classDefinition)
      println(s"Defined class is $definedClass")
      definedClass
    }

  }



}
