package Nebula.Generator

import Nebula.Schema.{ActorSchema, MethodSchema}
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

    // Generate class definition
    val tree = {
      //Import Akka Actor library
      BLOCK(IMPORT(ActorImport),
        //Actor definition starts here
        CLASSDEF(actorName).withParams(actorArguments).withParents(sym.Actor) := BLOCK(
          generateMethods(jsonSchema.methods, jsonSchema.methods.length - 1, methodDefinitionList)
        ),
        OBJECTDEF(actorName) := BLOCK(
          DEF("props",  "Props").withParams(actorArguments) := REF("Props") APPLY NEW(REF(actorName).APPLY(REF("actorName"), REF("actorAge")))),
        RETURN(REF(actorName) DOT("props") APPLY(LIT("hello"), LIT(0)))
      )
    }
    //Return the generated code as a String
    treeToString(tree)
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

}
