package NebulaScala3.Schema

case class ActorSchema(actorName: String, 
                       actorArgs: Seq[ArgumentSchema],
                       numOfInstances: Int,
                       methods: Seq[MethodSchema], 
                       monitoringOptions : CinnamonMonitoringSchema)


