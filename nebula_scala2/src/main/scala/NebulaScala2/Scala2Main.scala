package NebulaScala2

import akka.actor.Props
import kamon.Kamon

object Scala2Main  {
  val scala2Message = "Hello from Nebula Scala 2! :)"
  
  //Props
  var generatedActorsProps : Seq[Props] = _
  
  //This function start the Kamon monitoring framework
  def initKamon() : Unit = {
    Kamon.init()
  }
}
