package NebulaScala3.Generator

import NebulaScala3.Schema.{ArgumentSchema, MessageSchema}
import NebulaScala3.message.ProtoMessage


class ProtoMessageGenerator

object ProtoMessageGenerator {
  def generateProtoMessages(messageList: Array[MessageSchema],
                            iterator: Int,
                            protoMessages: Seq[ProtoMessage]
                           ): Seq[ProtoMessage] = {
    if (iterator >= messageList.length) protoMessages
    else generateProtoMessages(
      messageList, iterator + 1, protoMessages :+
        generateMessage(messageList(iterator))
    )
  }
  
  private def generateMessage(message: MessageSchema): ProtoMessage = {
    if (message.messageArgs != null) {
      var argTypeMap: Map[String, String] = Map()
      var argValuesMap: Map[String, String] = Map()
      message.messageArgs.foreach { arg
      =>
        argTypeMap += (arg.argName -> arg.argType)
        argValuesMap += (arg.argName -> arg.argValue)
      }
      ProtoMessage(message.messageName.toLowerCase, argTypeMap, argValuesMap)
    }
    else {
        ProtoMessage(message.messageName.toLowerCase, Map(), Map())
    }
  }
}

