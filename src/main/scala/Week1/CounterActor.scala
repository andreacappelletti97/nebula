package Week1

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import Week1.CounterActor.Counter.{Decrement, Increment, Print}

object CounterActor extends App{

  object Counter {
    case object Increment
    case object Decrement
    case object Print
  }

  class Counter extends Actor {
    var counter : Int = 0
    override def receive: Receive = {
      case Increment => counter+=1
      case Decrement => counter-=1
      case Print => println(s"The current counter is $counter")
    }
  }

  val system = ActorSystem("counterSystem")
  val counterActor = system.actorOf(Props[Counter](), "counter")
  counterActor ! Print
  counterActor ! Increment
  counterActor ! Print
  counterActor ! Decrement
  counterActor ! Print
  (1 to 5).foreach(_ => counterActor ! Increment)
  counterActor ! Print

}

