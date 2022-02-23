package NebulaScala2

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

object ConfigInjection  {
  def runConfigInject() = {
    // make a Config with just your special setting// make a Config with just your special setting

    val myConfig = ConfigFactory.parseString(
      """cinnamon {
        |  chmetrics = {
        |    reporters += "console-reporter"
        |  }
        |    akka.actors = {
        |    default-by-class {
        |      includes = "/user/*"
        |      report-by = class
        |    }
        |  }
        |  }
        |  cinnamon.prometheus {
        |  exporters += http-server
        |}""".stripMargin)

    val config2 = ConfigFactory.parseString(
      """cinnamon {
        |    akka.actors = {
        |    default-by-class {
        |      includes = "/user/*"
        |      report-by = class
        |    }
        |  }
        |  }
        |   cinnamon.prometheus {
        |  exporters += http-server
        |}""".stripMargin
    )

    //val myConfig = ConfigFactory.parseString("""""".stripMargin)
    // load the normal config stack (system props, then application.conf, then reference.conf)
    val regularConfig = ConfigFactory.load

    // override regular stack with myConfig
    val combined = myConfig.withFallback(regularConfig)

    // put the result in between the overrides (system props) and defaults again
    val complete = ConfigFactory.load(combined)

    // create ActorSystem with complete configuration
    //val systemWithConfig = ActorSystem("MySystem", ConfigFactory.load(complete))
    val actorSystem = ActorSystem("system", ConfigFactory.load(config2))

    //val simpleActorWithConfig = systemWithConfig.actorOf(Props[SimpleActor](), "simpleActorWithConfig")
    val simpleActor = actorSystem.actorOf(Props[SimpleActor](), "simpleActor")

    //simpleActorWithConfig ! "hello"
    simpleActor ! "hello"

  }

}
