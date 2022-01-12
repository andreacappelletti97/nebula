package TreeHugger.Generator

import TreeHugger.Schema.{ActorSchema, TypeName}
import treehugger.forest._
import treehuggerDSL._
import definitions._

class TreehuggerGenerator {
  //System definition
  object sym {
    val Actor = RootClass.newClass("Actor")
  }
  def generate(schema: ActorSchema): String = {
    //Register new type
    val classSymbol = RootClass.newClass(schema.actorName.shortName)
    //Generate list of constructor parameters
    val params = schema.fields.map { field =>
      val fieldName = field.name
      val fieldType = toType(field.valueType)
      PARAM(fieldName, fieldType): ValDef
    }
    // Generate class definition
    val tree = {
      BLOCK(IMPORT("akka.actor.Actor._"),
      CLASSDEF(classSymbol).withParams(params).withParents(sym.Actor).tree.withDoc(schema.comment):= BLOCK(
        DEF("printHello", StringClass) := LIT(ActorSchema.toJson(schema)),
        DEF("receive", StringClass) := Predef_println APPLY LIT("Hello, world!")

      )
    ).inPackage(schema.actorName.packageName)
    }

    //Return the generated code as a String
    treeToString(tree)
  }

  //Map Json defined types into TreeHugger modules
  private def toType(fieldType: TypeName): Type = {
    fieldType.fullName match {
      case "String" => StringClass
      case "Int" => IntClass
      case "Boolean" => BooleanClass
    }
  }

}
