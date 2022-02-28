package NebulaScala3.Schema

case class ActorDynamicSchema(actorName : String, 
                              actorArgs :  Seq[ArgumentSchema],
                              behavior : Seq[BehaviorSchema])
