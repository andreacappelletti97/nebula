package NebulaScala2

import kamon.Kamon

object Scala2Main  {
  val scala2Message = "Hello from Nebula Scala 2! :)"
  //This function start the Kamon monitoring framework
  def initKamon() : Unit = {
    Kamon.init()
  }
}
