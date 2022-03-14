package Factory

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object ClusterFactory {
  def createNode(port : Int, role : String, props : Props, actorName : String): Unit = {
    val config = ConfigFactory.parseString(
      s"""
         |akka.cluster.roles = ["$role"]
         |akka.remote.artery.canonical.port = $port
         |""".stripMargin
    ).withFallback(ConfigFactory.load("clusteringExample.conf"))
    val system = ActorSystem("Nebula", config)
    system.actorOf(props,actorName)
  }
}
