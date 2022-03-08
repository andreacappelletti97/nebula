package NebulaScala2

import akka.actor.{ActorRef, ActorSystem, Props}
import kamon.Kamon

object Scala2Main  {
  val scala2Message = "Hello from Nebula Scala 2! :)"
  
  //Props of the generated Actors
  var generatedActorsProps : Map[String, Props] = Map()

  //Global state to store actorSystems
  var generatedActorSystems : Map[String, ActorSystem] = Map()

  var generatedActorsRef : Map[String, ActorRef] = Map()
  
  //This function start the Kamon monitoring framework
  def initKamon() : Unit = {
    Kamon.init()
  }
}
