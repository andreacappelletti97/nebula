package NebulaScala3.Schema

/***
 * A case object is defined by a name without any argument
 * A case class is defined by a name and some arguments
 * Arguments are defined as @ArgumentSchema, that contains argName and argType
 ***/
case class MessageSchema(messageName: String, messageArgs: Seq[ArgumentSchema])