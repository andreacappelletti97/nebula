package NebulaScala3.Schema

case class DynamicMessageSchema(messageName: String,
                                messageContent : Seq[DynamicMessageContentSchema])
