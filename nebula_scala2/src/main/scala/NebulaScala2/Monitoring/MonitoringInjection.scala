package NebulaScala2.Monitoring

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

class MonitoringInjection

object MonitoringInjection {
  
  def injectMonitoringOptions(generatedConfig: String, actorSystemName: String) = {
    // load the normal config stack (system props, then application.conf, then reference.conf)
    val regularConfig = ConfigFactory.load
    val injectedConfig = ConfigFactory.parseString(generatedConfig)
    
    // override regular stack with injectedConfig
    val combined = injectedConfig.withFallback(regularConfig)

    // put the result in between the overrides (system props) and defaults again
    val complete = ConfigFactory.load(combined)

    val actorSystem = ActorSystem(actorSystemName, ConfigFactory.load(complete))
  }
  
}
