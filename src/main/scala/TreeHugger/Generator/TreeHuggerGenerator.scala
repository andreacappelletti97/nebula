package TreeHugger.Generator

import TreeHugger.Schema.TypeSchema
import TreeHugger.Schema.TypeName
import treehugger.forest._
import treehuggerDSL._
import definitions._

/**
 * An AST generator based on the 'treehugger' library.
 */
class TreehuggerGenerator {

  object sym {
    val Actor = RootClass.newClass("Actor")
  }

  def generate(schema: TypeSchema): String = {
    // register new type
    val classSymbol = RootClass.newClass(schema.name.shortName)

    // generate list of constructor parameters
    val params = schema.fields.map { field =>
      val fieldName = field.name
      val fieldType = toType(field.valueType)
      PARAM(fieldName, fieldType): ValDef
    }

    // generate class definition
    val tree = BLOCK(
      CLASSDEF(classSymbol).withParams(params).withParents(sym.Actor).tree.withDoc(schema.comment):= BLOCK(
        DEF("printHello", StringClass) := LIT(TypeSchema.toJson(schema)),
        DEF("receive", StringClass) := Predef_println APPLY LIT("Hello, world!")
      )
    ).inPackage(schema.name.packageName)

    // pretty print the tree
    treeToString(tree)
  }

  private def toType(fieldType: TypeName): Type = {
    fieldType.fullName match {
      case "String" => StringClass
      case "Int" => IntClass
      case "Boolean" => BooleanClass
    }
  }

}
