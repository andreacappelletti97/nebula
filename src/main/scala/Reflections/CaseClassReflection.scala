package Reflections
import scala.reflect.runtime
import scala.reflect.runtime.universe._
import scala.tools.reflect.ToolBox
object CaseClassReflection extends App {


  val columnList = "name:String,version:String"

  val runtimeMirror = runtime.currentMirror
  val toolBox = runtimeMirror.mkToolBox()
  val myCaseClass = toolBox.define(toolBox.parse(s"case class header($columnList)").asInstanceOf[ClassDef])


}
