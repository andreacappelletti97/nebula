package Week1

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import Week1.CounterActorStateless.Counter.{Decrement, Increment, Print}

object CounterActorStateless extends  App  {

  object Counter {
    case object Increment
    case object Decrement
    case object Print
  }

  class Counter extends Actor {
    var counter : Int = 0
    override def receive: Receive = countReceive(0)
    def countReceive(amount : Int) : Receive = {
      case Increment => context.become(countReceive(amount+1))
      case Decrement => context.become(countReceive(amount-1))
      case Print => println(s"Current amount is $amount")
    }
  }
  val system = ActorSystem("counterSystem")
  val counterActor = system.actorOf(Props[Counter](), "counter")

  (1 to 5).foreach(_ => counterActor ! Increment)
  (1 to 3).foreach(_ => counterActor ! Decrement)
  counterActor ! Print

}

