package Nebula.Generator

import Nebula.Schema.{ActorSchema, ArgumentSchema, CaseClassSchema, MethodSchema}
import TreeHugger.Generator.CodeGenerator.sym.ActorImport
import treehugger.forest._
import treehuggerDSL._
import definitions._

object ActorGenerator {
  //Standard system definition
  object sym {
    val Actor = RootClass.newClass("Actor")
    val ActorImport = "akka.actor._"
  }

  /***
   * This function generates the entire Scala code of the Actor from a JSON schema using the library TreeHugger
   * @param jsonSchema
   * @return
   */
  def generateActor(jsonSchema: ActorSchema): String = {
    //Retrieve the Actor Name
    val actorName = RootClass.newClass(jsonSchema.actorName)
    //Generate list of Actor arguments
    val actorArguments = jsonSchema.actorArgs.map { arg =>
      val argName = arg.argName
      val argType = arg.argType
      PARAM(argName, argType): ValDef
    }
    //Init of the method for the recursive function population
    val methodDefinitionList = Seq.empty[DefDef]
    val propsValuesList = Seq.empty[Literal]
    val refList = Seq.empty[RefTree]

    // Generate class definition
    val tree = {
      //Import Akka Actor library
      BLOCK(IMPORT(ActorImport),
        //Actor definition starts here
        CLASSDEF(actorName).withParams(actorArguments).withParents(sym.Actor) := BLOCK(
          generateMethods(jsonSchema.methods, jsonSchema.methods.length - 1, methodDefinitionList)
        ),
        //Define the companion object for Actor initialisation
        OBJECTDEF(actorName) := BLOCK(
          DEF("props",  "Props").withParams(actorArguments) := REF("Props") APPLY NEW(REF(actorName).APPLY(
            generateActoref(jsonSchema.actorArgs, 0, refList)
          ))),
        RETURN(REF(actorName) DOT("props") APPLY(generateActorPropsValues(jsonSchema.actorArgs, 0, propsValuesList)))
      )
    }
    //Return the generated code as a String
    treeToString(tree)
  }


  def generateActoref(jsonRefList : Seq[ArgumentSchema], iterator: Int, refList : Seq[RefTree]): Seq[RefTree] = {
    if(iterator >= jsonRefList.length) refList
    else {
      generateActoref(jsonRefList, iterator + 1, refList :+
        REF(jsonRefList(iterator).argName)
      )
    }

  }

  //Generate actor props values
  def generateActorPropsValues(jsonPropsValues: Seq[ArgumentSchema], iterator: Int, propsValuesList: Seq[Literal]): Seq[Literal] = {
    if(iterator >= jsonPropsValues.length) propsValuesList
    else {
      generateActorPropsValues(jsonPropsValues, iterator + 1, propsValuesList :+
        LIT(jsonPropsValues(iterator).propsValue)
      )
    }
  }

  /***
   * This recursive function generates the methods defined into the declarative language
   * @param jsonMethods
   * @param iterator
   * @param methodsList
   * @return
   */
  def generateMethods(jsonMethods: Seq[MethodSchema], iterator: Int, methodsList : Seq[DefDef]) : Seq[DefDef] = {
    if (iterator < 0) methodsList
    else {
      if (jsonMethods(iterator).methodName == "receive") {
        generateMethods(
          jsonMethods, iterator - 1,
          methodsList :+ (
            DEF("receive", "Receive") withFlags (Flags.OVERRIDE) :=
              BLOCK(
                CASE(SOME(ID("x"))) ==> REF("x"),
                CASE(NONE) ==> BLOCK(Predef_println APPLY LIT("None Received!"))
              )
            )
        )
      } else {
        generateMethods(jsonMethods, iterator - 1, methodsList)
      }
    }
  }

  //TODO: automatically populate caseList
  def generateCases(jsonCaseList : Seq[CaseClassSchema], caseList: Seq[DefDef]): Seq[DefDef] = {
    jsonCaseList.foreach(jsonCase => {

    })
    null
  }

}
