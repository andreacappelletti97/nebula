package Actors

import akka.actor._

class HelloActor(myName: String) extends Actor {
  def receive = {

    case "hello" => println("hello from %s".format(myName))
    case _       => println("'huh?', said %s".format(myName))
  }
}


class HelloActor2(myName : String) extends  Actor {
  override def receive: Receive = {
    case "hey" => println("ciao!!!")
  }
}

object HelloActor extends App {
  val system = ActorSystem("HelloSystem")

  val helloActor = system.actorOf(Props(new HelloActor("Fred")), name = "helloactor")
  val hello2Actor = system.actorOf(Props(new HelloActor2("Hello2")), name = "helloactor2")

  hello2Actor ! "hey"

  while(true) {
    helloActor ! "hello"
    Thread.sleep(3000) // wait for 2 seconds
  }
  helloActor ! "buenos dias"

}