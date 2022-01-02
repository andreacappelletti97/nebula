package Reflections

import scala.reflect.runtime.universe
import scala.tools.reflect.ToolBox

object ActorCompiler extends App{
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

  protected def executePayload(map: Map[String, Any], payload: String): Any = {
    if (payload.length == 0) {
      return ()
    }
    val f = compile[Option[Any]](payload)
    f(map) match {
      case Some(value) => value
      case None => ()
    }
  }

}
