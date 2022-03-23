package NebulaScala3.Schema

case class ActorMonitoring(
                          path : String,
                          reporter : String,
                          thresholds : ThresholdMonitoringSchema
                          )
