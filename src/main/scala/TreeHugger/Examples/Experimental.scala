package TreeHugger.Examples

import scala.reflect.runtime.{currentMirror, universe}
import scala.reflect.runtime.universe._
import scala.tools.reflect.ToolBox

class Experimental

object Experimental extends App {
  val toolbox = currentMirror.mkToolBox()
  // staticEnvironmentCompilation()
  implDefUsage()

  def implDefUsage() = {
    val tb = universe.runtimeMirror(getClass.getClassLoader).mkToolBox()
    //tb.eval(tb.parse("""println("hello!")"""))
    val function: Tree = q"val x = 5"

    val myImpl : ImplDef = q"object FunctionWrapper { $function }".asInstanceOf[ImplDef]
    val functionWrapper  = tb.define(myImpl)

    println(functionWrapper)
    val number = q"$functionWrapper.x + 5"
    println(tb.eval(number))
    //val data: List[Tree] = List(q"1", q"2")
    //println(data.map(x => tb.eval(q"$functionWrapper.function($x)"))) // List(3, 4)


  }

  case class X(a: Int, b: Int)
  val x = X(1, 2)
  def caseClassImport() = {
    val tree = toolbox.parse("import TreeHugger.Examples.Experimental._; val x1 = X(x.a + 1, x.b + 1); return x1")
    val tree2 = toolbox.parse("println(x1)")
    val res = toolbox.eval(tree)
    val res2 = toolbox.eval(tree2)
    println(res)
    println(res2)
  }


  def staticEnvironmentCompilation(): Unit = {
    val definition  = q"val x = 5"
    define(definition.asInstanceOf[ImplDef])

    //eval[Unit]("println(x)")
  }

  def define(code: ImplDef) = {
    toolbox.define(code)
  }

  def eval[T](code: String): T = {
    val evaluated = toolbox.eval(toolbox.parse(code)).asInstanceOf[T]
    println(evaluated)
    evaluated
  }




}
