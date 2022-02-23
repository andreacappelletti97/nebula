package NebulaScala2

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

object ConfigInjection  {
  def runConfigInject() = {
    val actorSystem = ActorSystem("system")

    // make a Config with just your special setting// make a Config with just your special setting
    
    val myConfig = ConfigFactory.parseString(
      """cinnamon {
        |  chmetrics = {
        |    reporters += "console-reporter"
        |  }
        |  }
        |  cinnamon.prometheus {
        |  exporters += http-server
        |}""".stripMargin)
    
    //val myConfig = ConfigFactory.parseString("""""".stripMargin)
    // load the normal config stack (system props, then application.conf, then reference.conf)
    val regularConfig = ConfigFactory.load

    // override regular stack with myConfig
    val combined = myConfig.withFallback(regularConfig)

    // put the result in between the overrides (system props) and defaults again
    val complete = ConfigFactory.load(combined)

    // create ActorSystem with complete configuration
    val system = ActorSystem("MySystem", ConfigFactory.load(complete))

    val simpleActor = system.actorOf(Props[SimpleActor](), "simpleActor")
  }

}
