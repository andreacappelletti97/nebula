package TreeHugger.Examples

import akka.japi.pf.FI
import treehugger.forest._
import definitions._
import treehuggerDSL._
import treehugger.forest._
import definitions._
import treehuggerDSL._
class TreeHuggerExamples

object TreeHuggerExamples extends App {

  def printTree(tree: Tree) = println(treeToString(tree))

  object sym {
    val IntQueue = RootClass.newClass("IntQueue")
    val BasicIntQueue = RootClass.newClass("BasicIntQueue")
    val buf = BasicIntQueue.newValue("buf")
  }


  //Println basic statement instance
  val tree : Tree = Predef_println APPLY LIT("Hello, world!")
  //printTree(tree)

  val tree1 : Tree = CLASSDEF(sym.IntQueue) withFlags(Flags.ABSTRACT) :=
    BLOCK(
      DEF("get", IntClass),
      PROC("put") withParams(PARAM("x", IntClass))
     )
  printTree(tree1)

  //What's the difference between DEF and PROC ?

  val tree2 : Tree = CLASSDEF(sym.BasicIntQueue) withParents(sym.IntQueue) :=
    BLOCK(
      VAL(sym.buf) withFlags(Flags.PRIVATE) :=
        NEW(ArrayBufferClass TYPE_OF IntClass),
      DEF("get", IntClass) := REF(sym.buf) DOT "remove" APPLY(),
      PROC("put") withParams(PARAM("x", IntClass)) := BLOCK(
        REF(sym.buf) INFIX("+=") APPLY REF("x")
      )
    )
  printTree(tree2)

  //Explore more about REF

  val tree3 : Tree = (VAL("foo", IntClass) : Tree)
  printTree(tree3)

  val tree4 : Tree = (DEF("x", "Receive") : Tree)
  printTree(tree4)

  //Case class init
  val tree5 : Tree = (CASECLASSDEF(RootClass.newClass("Var"))
    withParams(PARAM("name", StringClass)) withParents(RootClass.newClass("Expr")): Tree)
  printTree(tree5)


}
