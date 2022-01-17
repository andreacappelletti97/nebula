package Reflections
import scala.reflect.runtime.universe
import scala.reflect.runtime.universe.{ImplDef, Quasiquote, Tree}
import scala.tools.reflect.ToolBox


object SimpleReflection extends App{

  val tb = universe.runtimeMirror(getClass.getClassLoader).mkToolBox()
  tb.eval(tb.parse("""println("hello!")"""))
  val function: Tree = q"def function(x: Int): Int = x + 2"

  val myImpl : ImplDef = q"object FunctionWrapper { $function }".asInstanceOf[ImplDef]
  val functionWrapper  = tb.define(myImpl)

  println(functionWrapper)
  val data: List[Tree] = List(q"1", q"2")
  println(data.map(x => tb.eval(q"$functionWrapper.function($x)"))) // List(3, 4)


  /*
  val myActor : Tree = q"import akka.actor._; class MyActor extends Actor {override def receive : Receive = { case Some(x) => x } => x}"
  val actor = tb.compile(myActor)
  println(actor)

  val myObj : ImplDef = q"object MyActor { def props() = Props(new MyActor) }".asInstanceOf[ImplDef]
  val functionWrapper2  = tb.define(myObj)
*/
}
