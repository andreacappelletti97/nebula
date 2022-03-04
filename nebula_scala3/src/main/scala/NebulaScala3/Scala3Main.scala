package NebulaScala3

import NebulaScala3.Schema.{DynamicMessageContentSchema, DynamicMessageSchema}
import com.github.os72.protobuf.dynamic.DynamicSchema
import com.google.protobuf.DynamicMessage

//In this object we declare all the global mutable variables
object Scala3Main:
  val scala3Message = "Hello from Nebula Scala 3! :)"
  var xxx: Int = _
  //Message definition
  trait SomeTrait
  var theKeeper: SomeTrait = _

  type Proto = DynamicMessage
  
  case class Authentication(email:String)
  val obj = Authentication("test")

  //This wrapper contains the dynamic protobuf builders to instantiate at run-time
  var dynamicMessagesBuilders : Seq[DynamicSchema] = _

  //This structure stores the dynamic messages from the DSL input, it keeps track of their changes
  var dynamicMessages : Array[DynamicMessageSchema] = _