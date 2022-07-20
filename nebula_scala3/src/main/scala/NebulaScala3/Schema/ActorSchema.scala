package NebulaScala3.Schema

case class ActorSchema(
    actorName: String,
    actorArgs: Seq[ArgumentSchema],
    actorVars: Seq[VariableSchema],
    methods: Seq[MethodSchema]
)
