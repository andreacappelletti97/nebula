package Examples

import scala.reflect.runtime._
import scala.reflect.runtime.universe._
import scala.tools.reflect.ToolBox

object ReflectionTestApp extends App {

  val cm = universe.runtimeMirror(getClass.getClassLoader)
  val toolBox = cm.mkToolBox()

  val sourceClass = toolBox.eval(
    toolBox.parse(
      s"""
         | case class Employee(_id: Int, _name: String) {
         |    def id = _id
         |    def name = _name
         | }
         | scala.reflect.classTag[Employee].runtimeClass
       """.stripMargin))
    .asInstanceOf[Class[_]]

  val targetClass = toolBox.eval(
    toolBox.parse(
      s"""
         | case class Result(_success: Boolean) {
         |    def success = _success
         | }
         | scala.reflect.classTag[Result].runtimeClass
       """.stripMargin
    )).asInstanceOf[Class[_]]

  val sourceConstructor = sourceClass.getConstructors()(0)
  val sourceObject = sourceConstructor.newInstance(Seq(1.asInstanceOf[Object], "Subhobrata Dey".asInstanceOf[Object]): _*)

  println(sourceObject)

  val code =
    q"""
       (sourceClass: Class[_], sourceObject: Any, targetClass: Class[_]) => {
          val id = sourceClass.getMethod("id").invoke(sourceObject)
          if (id.toString().equals("1")) {
            val targetConstructor = targetClass.getConstructors()(0)
            Some(targetConstructor.newInstance(Seq(true.asInstanceOf[Object]): _*))
          } else None
       }
     """

  val compiledCode = toolBox.compile(code)
  val compiledFunc = compiledCode().asInstanceOf[(Class[_], Any, Class[_]) => Option[Any]]

  println(compiledFunc(sourceClass, sourceObject, targetClass))
}
