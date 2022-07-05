package NebulaScala3.Generator

import NebulaScala3.Schema.{CinnamonActorMonitoringSchema, CinnamonMonitoringSchema, ClusterSchema, ThresholdMonitoringSchema}

import scala.annotation.tailrec

class ConfigCodeGenerator

object ConfigCodeGenerator:

  def generateClusterConfigCode(clusterConfigSchema : ClusterSchema): String =
      s"""
        |akka {
        |  actor{
        |    provider = cluster
        |  }
        |  remote {
        |    artery{
        |      enabled = on
        |      transport = ${clusterConfigSchema.transport}
        |      canonical.hostname = "${clusterConfigSchema.hostname}"
        |      canonical.port = ${clusterConfigSchema.port}
        |    }
        |  }
        |  cluster{
        |    seed-nodes = [
        |      ${clusterConfigSchema.seedNodes.map{e => '"' + e + '"'}.mkString(",")}
        |    ]
        |  }
        |}
        |""".stripMargin

  
  def generateMonitoringConfig(configSchema : CinnamonMonitoringSchema) : String =
    s"""cinnamon {
      |${consoleReporter(configSchema.consoleReporter)}
      |akka.actors {
      |${generateAkkaActorsMonitoringConfig(configSchema.actors, 0, Seq.empty, configSchema.messageType).mkString(",")}
      |}
      |${includeDefaultMetrics()}
      |}
      |${prometheusHttp(configSchema.prometheusHttpServer)}
      |""".stripMargin
  
  //Enable in the entire JVM
  private def prometheusHttp(enabled : Boolean) =
    if(enabled) """cinnamon.prometheus {
                  |  exporters += http-server
                  |}""".stripMargin
    else ""
      
  //Enable in the entire JVM
  private def consoleReporter(enabled : Boolean) =
    if(enabled) """chmetrics = {
                  |    reporters += "console-reporter"
                  |  }""".stripMargin
    else ""

  @tailrec
  private def generateAkkaActorsMonitoringConfig(actorMonitoringOptions: Seq[CinnamonActorMonitoringSchema], iterator: Int, actorMonitoringConfig: Seq[String], messageType: Boolean): Seq[String] = {
    if (iterator >= actorMonitoringOptions.size) actorMonitoringConfig
    else {
      generateAkkaActorsMonitoringConfig(
        actorMonitoringOptions,
        iterator + 1,
        actorMonitoringConfig :+
          s""""${actorMonitoringOptions(iterator).path}" {
             |${if (messageType) "message-type = on" else ""}
             |report-by = ${actorMonitoringOptions(iterator).reporter}
             |}
             |""".
            stripMargin,
        messageType
      )
    }
  }

  private def includeDefaultMetrics(): String =
    """akka.dispatchers = {
      |    basic-information {
      |      names = ["*"]
      |    }
      |    time-information {
      |      names = ["*"]
      |    }
      |  }
      |
      |  akka.remote = {
      |    serialization-timing = on
      |    failure-detector-metrics = on
      |  }
      |
      |  akka.cluster = {
      |    domain-events = on
      |    node-metrics = on
      |    member-events = on
      |    singleton-events = on
      |    shard-region-info = on
      |  }
      |
      |  akka.http = {
      |    servers {
      |      "*:*" {
      |        paths {
      |          "*" {
      |            metrics = on
      |          }
      |        }
      |      }
      |    }
      |    clients {
      |      "*:*" {
      |        paths {
      |          "*" {
      |            metrics = on
      |          }
      |        }
      |      }
      |    }
      |  }""".stripMargin

//  private def generateThresholds(thresholds: ThresholdMonitoringSchema): String = {
//    if(thresholds.stashSize == 0)
//     s"""
//      |thresholds {
//      |${if(thresholds.mailboxSize != 0) s"mailbox-size = ${thresholds.mailboxSize}"}
//      |${if (thresholds.mailboxTime != 0) s"mailbox-time = ${thresholds.mailboxTime}s"}
//      |${if (thresholds.stashSize != 0) s"stash-size = ${thresholds.stashSize}"}
//      |${if (thresholds.processingTime != 0) s"processing-time = ${thresholds.processingTime}"}
//      |}
//      |""".stripMargin
//  }



    

      
    
  
