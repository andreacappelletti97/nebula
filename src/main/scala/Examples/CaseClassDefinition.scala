package Examples

import scala.reflect.runtime._
import scala.reflect.runtime.universe._
import scala.tools.reflect.ToolBox

object CaseClassDefinition extends App{

  val cm = universe.runtimeMirror(getClass.getClassLoader)
  val toolBox = cm.mkToolBox()

  val caseClassCode = q"""case class Authentication(email:String)""".asInstanceOf[ImplDef]
  val definedCaseClass = toolBox.define(caseClassCode)
  println(definedCaseClass)

  val sourceClass = toolBox.eval(
    toolBox.parse(
      s"""
         | case class Authentication(email: String)
         | scala.reflect.classTag[Authentication].runtimeClass
       """.stripMargin))
    .asInstanceOf[Class[_]]

  println(sourceClass)

  val tb = universe.runtimeMirror(getClass.getClassLoader).mkToolBox()
  val function: ClassDef = q"case class Film(title: String)".asInstanceOf[ClassDef]
  val functionWrapper  = tb.define(function)
  println(functionWrapper)
  val newFilm = q"""new $functionWrapper("Kingdom")"""
  println(tb.eval(newFilm))


}
