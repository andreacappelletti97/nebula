package NebulaScala2.Persistence

import NebulaScala2.SimpleActor
import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object Cassandra {

  def runCassandra() = {
    val cassandraActorSystem = ActorSystem("cassandraSystem", ConfigFactory.load().getConfig("cassandraDemo"))
    val persistentActor = cassandraActorSystem.actorOf(Props[SimplePersistentActor], "simpleActor")

    for (i <- 1 to 10) {
      persistentActor ! s"I love Akka [$i]"
    }
    persistentActor ! "print"
    persistentActor ! "snap"

    for (i <- 11 to 20) {
      persistentActor ! s"I love Akka [$i]"
    }
  }
}
