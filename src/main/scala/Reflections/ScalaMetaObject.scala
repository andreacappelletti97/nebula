package Reflections
import scala.meta._

object ScalaMetaObject extends App{
  //Write your source code
  val program = """object Main extends App { print("Hello!") }"""
  //Generate the syntax tree
  val tree = program.parse[Source].get
  //Print the tree and the original source code
  println(tree.syntax)
  //Parse an expression
  println("a + b".parse[Stat].get.structure)
  //Parse a type
  println("A with B".parse[Type].get.structure)

}
