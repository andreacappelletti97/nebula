package NebulaScala3.Generator

import NebulaScala3.Schema.CinnamonMonitoringSchema

class ConfigCodeGenerator

object ConfigCodeGenerator:
  //This function generates the config settings for the ActorSystems
  def generateConfigCode(configSchemaList : Seq[CinnamonMonitoringSchema]) : Seq[String] = ???
  
  private def generateConfig(configSchema : CinnamonMonitoringSchema) : String =
    s"""cinnamon {
      |${consoleReporter(configSchema.consoleReporter)}
      |${akkaActors()}
      |}
      |${prometheusHttp(configSchema.prometheusHttpServer)}
      |""".stripMargin
  
  //Enable in the entire JVM
  private def prometheusHttp(enabled : Boolean) =
    if(enabled) """cinnamon.prometheus {
                  |  exporters += http-server
                  |}""".stripMargin
      
  //Enable in the entire JVM
  private def consoleReporter(enabled : Boolean) =
    if(enabled) """chmetrics = {
                  |    reporters += "console-reporter"
                  |  }""".stripMargin

  private def akkaActors() =
    """akka.actors{
      |
      |}
      |""".stripMargin


    

      
    
  
