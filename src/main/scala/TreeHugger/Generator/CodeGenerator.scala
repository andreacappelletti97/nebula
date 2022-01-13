package TreeHugger.Generator

import TreeHugger.Generator.CodeGenerator.sym.ActorImport
import TreeHugger.Schema.{ActorTemplate}
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

    def actorMethods() : DefDef = {
      jsonSchema.methods.foreach(
        method => {
          if (method.methodName == "receive") {
             DEF("receive", "Receive") withFlags (Flags.OVERRIDE) :=
              BLOCK(
                CASE(SOME(ID("x"))) ==> REF("x"),
                CASE(NONE) ==> BLOCK(Predef_println APPLY LIT("None Received!"))
              )
          }
        })
       DEF("receive", "Receive") withFlags (Flags.OVERRIDE) :=
        BLOCK(
          CASE(SOME(ID("x"))) ==> REF("x"),
          CASE(NONE) ==> BLOCK(Predef_println APPLY LIT("None Received!"))
        )
    }

    // Generate class definition
    val tree = {
      //Import Akka Actor library
      BLOCK(IMPORT(ActorImport),
        //Actor definition starts here
        CLASSDEF(actorName).withParams(actorArguments).withParents(sym.Actor) := BLOCK(
          actorMethods()
        ),
        OBJECTDEF(actorName) := BLOCK(
          DEF("props",  "Props").withParams(actorArguments) := REF("Props") APPLY NEW(REF(actorName).APPLY(REF("myName"), REF("myIntParam")))),
        RETURN(REF(actorName) DOT("props") APPLY(LIT("hello"), LIT(0)))
      )
    }
    //Return the generated code as a String
    treeToString(tree)
  }

}
