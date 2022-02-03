package Nebula

import scala.reflect.runtime.universe
import scala.reflect.runtime.universe.{ClassDef, ValDef}
import scala.tools.reflect.ToolBox

object Example extends App {
  val toolbox = universe.runtimeMirror(getClass.getClassLoader).mkToolBox()
  val myCode = toolbox.eval(toolbox.parse("case class Film(title: String)"))
  println(toolbox.eval(toolbox.parse("""Film("Kingdom")""")))

}
