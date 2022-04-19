package NebulaScala3

import NebulaScala3.message.ProtoMessage

object Scala3Main:
  // Init message for integration sync
  val scala3Message = "Hello from Nebula Scala 3! :)"
  //Mutable var to store generate messages from Messages.json 
  var protoBufferList : Map[String, ProtoMessage] = Map()


