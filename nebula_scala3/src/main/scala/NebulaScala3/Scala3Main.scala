package NebulaScala3

import com.github.os72.protobuf.dynamic.DynamicSchema

//In this object we declare all the global mutable variables
object Scala3Main:
  val scala3Message = "Hello from Nebula Scala 3! :)"
  var xxx: Int = _
  //Message definition
  trait SomeTrait
  var theKeeper: SomeTrait = _

  //This wrapper contains the dynamic protobuf builders to instantiate at run-time
  var dynamicMessagesBuilders : Seq[DynamicSchema] = _
