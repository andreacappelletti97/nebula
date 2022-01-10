package Reflections
import treehugger.forest._, definitions._, treehuggerDSL._


object TreeHuggerObject extends App {
  val tree: Tree = Predef_println APPLY LIT("Hello, world!")
  println(tree)
  println(treeToString(tree))

}

object TreeHuggerObject2 extends App {
  object sym {
    val IntQueue = RootClass.newClass("IntQueue")
    val BasicIntQueue = RootClass.newClass("BasicIntQueue")
    val buf = BasicIntQueue.newValue("buf")
  }

  val tree1 = CLASSDEF(sym.IntQueue) withFlags(Flags.ABSTRACT) := BLOCK(
    DEF("get", IntClass),
    PROC("put") withParams(PARAM("x", IntClass))
  )

  println(treeToString(tree1))

  val tree2 = CLASSDEF(sym.BasicIntQueue) withParents(sym.IntQueue) := BLOCK(
    VAL(sym.buf) withFlags(Flags.PRIVATE) :=
      NEW(ArrayBufferClass TYPE_OF IntClass),
    DEF("get", IntClass) := REF(sym.buf) DOT "remove" APPLY(),
    PROC("put") withParams(PARAM("x", IntClass)) := BLOCK(
      REF(sym.buf) INFIX("+=") APPLY REF("x")
    )
  )

  println(treeToString(tree2))
}

object TreeHuggerObject3 extends App {
  object sym {
    val BasicIntQueue = RootClass.newClass("BasicIntQueue")
    val buf = BasicIntQueue.newValue("buf")
    val A = ArrowAssocClass.newTypeParameter("A")
    val arrow = ArrowAssocClass.newMethod("e->")
    val B = ArrowAssocClass.newTypeParameter("B")
    val T = BasicIntQueue.newAliasType("T")
  }





}

