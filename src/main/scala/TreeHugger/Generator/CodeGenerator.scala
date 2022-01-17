package TreeHugger.Generator

import TreeHugger.Generator.CodeGenerator.sym.ActorImport
import TreeHugger.Schema.{ActorTemplate, MethodTemplate}
import treehugger.forest._
import treehuggerDSL._
import definitions._

object CodeGenerator {
  //System definition
  object sym {
    val Actor = RootClass.newClass("Actor")
    val ActorImport = "akka.actor._"
  }
  def generate(jsonSchema: ActorTemplate): String = {
    //Retrieve the Actor Name
    val actorName = RootClass.newClass(jsonSchema.actorName)
    //Generate list of Actor arguments
    val actorArguments = jsonSchema.actorArgs.map { arg =>
      val argName = arg.argName
      val argType = arg.argType
      PARAM(argName, argType): ValDef
    }

    def actorMethods(jsonMethods: Seq[MethodTemplate], iterator: Int, methodsList : Seq[DefDef]) : Seq[DefDef] = {
      if (iterator < 0) methodsList
      else {
        if (jsonMethods(iterator).methodName == "receive") {
          actorMethods(
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
          actorMethods(jsonMethods, iterator - 1, methodsList)
        }
      }
    }

    val methodDefinitionList = Seq.empty[DefDef]

    // Generate class definition
    val tree = {
      //Import Akka Actor library
      BLOCK(IMPORT(ActorImport),
        //Actor definition starts here
        CLASSDEF(actorName).withParams(actorArguments).withParents(sym.Actor) := BLOCK(
          actorMethods(jsonSchema.methods, jsonSchema.methods.length - 1, methodDefinitionList)
        ),
        OBJECTDEF(actorName) := BLOCK(
          DEF("props",  "Props").withParams(actorArguments) := REF("Props") APPLY NEW(REF(actorName).APPLY(REF("actorName"), REF("actorAge")))),
        RETURN(REF(actorName) DOT("props") APPLY(LIT("hello"), LIT(0)))
      )
    }
    //Return the generated code as a String
    treeToString(tree)
  }

}
