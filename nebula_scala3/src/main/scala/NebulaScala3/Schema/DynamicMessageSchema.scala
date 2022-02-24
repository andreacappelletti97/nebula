package NebulaScala3.Schema

case class DynamicMessageSchema(messageName: String,
                                messageArgs : Seq[DynamicMessageArgumentSchema])
