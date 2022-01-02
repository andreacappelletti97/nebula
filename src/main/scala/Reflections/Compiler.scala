package Reflections

import scala.reflect.runtime.universe
import scala.tools.reflect.ToolBox


//https://medium.com/@kadirmalak/compile-arbitrary-scala-code-at-runtime-using-reflection-with-variable-injection-2002e0500565
object Compiler extends App {
  private def compile[A](code: String): (Map[String, Any]) => A = {
    val tb = universe.runtimeMirror(getClass.getClassLoader).mkToolBox()
    val tree = tb.parse(
      s"""
         |def wrapper(context: Map[String, Any]): Any = {
         |  $code
         |}
         |wrapper _
    """.stripMargin)
    val f = tb.compile(tree)
    val wrapper = f()
    wrapper.asInstanceOf[Map[String, Any] => A]
  }

  val code =
    """
      |val a = context("a").asInstanceOf[Int]
      |val b = context("b").asInstanceOf[String]
      |println(a)
      |println(b)
      |"result string"
  """.stripMargin

  val f = compile[String](code) // return type of wrapper is String
  val res = f(Map("a" -> 5, "b" -> "hello"))
  println(res)


}
