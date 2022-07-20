package Nebula

import akka.actor.{ActorSystem, Props}
import NebulaScala2.Scala2Main.generatedActorSystems
import com.typesafe.config.ConfigFactory
import NebulaScala3.Parser.{JSONParser, YAMLParser}

class ActorSystemFactory

object ActorSystemFactory {
  //This function is used from the Orchestrator to init actorSystems and store them
  def initActorSystem(name : String, configString: String, cluster: Boolean, clusterJsonPath: String): ActorSystem = {
    if (configString.isEmpty) {
      val actorSystem = ActorSystem(name)
      generatedActorSystems += name -> actorSystem
      actorSystem
    } else {
      val config = ConfigFactory.parseString(
        configString
      ).resolve()
      if(!cluster) {
        val actorSystem = ActorSystem(name, config)
        generatedActorSystems += name -> actorSystem
        actorSystem
      } else {
        val clusterJson = JSONParser.getClusterShardingSchemaFromJson(clusterJsonPath)
        clusterJson.initPorts.foreach{port =>
          startCluster(port, configString)
        }
        val config = ConfigFactory.parseString(
          s"""
             |akka.remote.artery.canonical.port = 0
             |""".stripMargin
        ).withFallback(
          ConfigFactory.load(
            configString
          )
        )
        //All the actorSystem names in a cluster should be the same
        val system = ActorSystem("Nebula", config)
        system
      }
    }
  }

  //This function start the Akka Cluster Sharding Nebula scenario
  def startCluster(port :Int, myConfig:String): ActorSystem = {
        val config = ConfigFactory.parseString(
          s"""
             |akka.remote.artery.canonical.port = $port
             |""".stripMargin
        ).withFallback(
          ConfigFactory.load(
            myConfig
          )
        )
        //All the actorSystem names in a cluster should be the same
        val system = ActorSystem("Nebula", config)
        system
  }

}
