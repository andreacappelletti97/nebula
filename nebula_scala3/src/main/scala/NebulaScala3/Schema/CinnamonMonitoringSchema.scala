package NebulaScala3.Schema


case class CinnamonMonitoringSchema(consoleReporter : Boolean,
                                    prometheusHttpServer : Boolean,
                                    messageType : Boolean,
                                    actors: Seq[CinnamonActorMonitoringSchema]
                                   )
