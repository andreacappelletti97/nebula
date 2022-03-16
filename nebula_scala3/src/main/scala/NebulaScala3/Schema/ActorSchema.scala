package NebulaScala3.Schema

case class ActorSchema(actorName: String, 
                       actorArgs: Seq[ArgumentSchema], 
                       methods: Seq[MethodSchema], 
                       monitoringOptions : CinnamonMonitoringSchema)


