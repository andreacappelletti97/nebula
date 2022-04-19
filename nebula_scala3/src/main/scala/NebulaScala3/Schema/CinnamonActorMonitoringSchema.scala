package NebulaScala3.Schema

case class CinnamonActorMonitoringSchema(
                                          path: String,
                                          reporter: String,
                                          thresholds: ThresholdMonitoringSchema
                                        )
