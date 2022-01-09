package Reflections
import treehugger.forest._, definitions._, treehuggerDSL._
import treehugger.forest._
import definitions._
import treehuggerDSL._

object TreeHuggerObject extends App {
  val tree: Tree = Predef_println APPLY LIT("Hello, world!")
  println(tree)
  println(treeToString(tree))


}
