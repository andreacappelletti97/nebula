package Nebula

import akka.actor.{ActorSystem, Props}
import NebulaScala2.Scala2Main.generatedActorSystems
import com.typesafe.config.ConfigFactory

class ActorSystemFactory

object ActorSystemFactory {
  //This function is used from the Orchestrator to init actorSystems and store them
  def initActorSystem(name : String): Unit = {
    val actorSystem = ActorSystem(name)
    generatedActorSystems += name -> actorSystem
  }

  //This function start the Akka Cluster Sharding Nebula scenario
  def startCluster(ports : List[Int]): Unit = {
    def startCluster(ports : List[Int]): Unit = {
      ports.foreach{port =>
        val config = ConfigFactory.parseString(
          s"""
             |akka.remote.artery.canonical.port = $port
             |""".stripMargin
        ).withFallback(
          ConfigFactory.load(
            "clustering/cluster.conf"
          )
        )
        //All the actorSystem names in a cluster should be the same
        val system = ActorSystem("Nebula", config)
      }
    }
    startCluster(List(2551, 2552, 0))
    //0 -> system allocates the port for you
  }
}
