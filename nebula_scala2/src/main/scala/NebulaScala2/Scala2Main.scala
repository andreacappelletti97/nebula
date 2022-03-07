package NebulaScala2

import NebulaScala3.message.ProtoMessage
import akka.actor.Props
import kamon.Kamon

object Scala2Main  {
  val scala2Message = "Hello from Nebula Scala 2! :)"
  
  //Props of the generated Actors
  var generatedActorsProps : Map[String, Props] = Map()
  
  //This function start the Kamon monitoring framework
  def initKamon() : Unit = {
    Kamon.init()
  }
}
