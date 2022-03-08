package NebulaScala3.Generator

import NebulaScala3.Schema.{ArgumentSchema, MessageSchema}
import NebulaScala3.message.ProtoMessage


class ProtoMessageGenerator

object ProtoMessageGenerator {

  //This function test the protoMessageGenerator functionalities
  private def testProtoMessageGenerator : Unit = {
      println(generateProtoMessages(
        Array(
          MessageSchema("Auth", Seq(ArgumentSchema("email", "String", ""))),
          MessageSchema("Film", Seq(ArgumentSchema("title", "String", ""))),
        ),
        0,
        Seq.empty
      ))
    }

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
      var argMap : Map[String, String] = Map()
      message.messageArgs.foreach(arg
      => argMap += (arg.argName -> arg.argType)
      )
      return ProtoMessage(message.messageName,argMap)
    }
}

