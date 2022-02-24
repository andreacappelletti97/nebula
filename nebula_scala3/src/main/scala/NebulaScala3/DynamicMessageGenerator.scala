package NebulaScala3

import NebulaScala3.Schema.{DynamicMessageContentSchema, DynamicMessageSchema}
import com.github.os72.protobuf.dynamic.{DynamicSchema, MessageDefinition}

class DynamicMessageGenerator

object DynamicMessageGenerator:

  @main def main =
    val dynamicMessageSchema : DynamicMessageSchema = DynamicMessageSchema(
      "Person",
      Seq(DynamicMessageContentSchema(
        "required",
        "name",
        "string"
      ))
    )
    generateMessage(dynamicMessageSchema)


  def generateMessage(dynamicMessage : DynamicMessageSchema) =
    val schemaBuilder = DynamicSchema.newBuilder
    schemaBuilder.setName("PersonSchemaDynamic.proto")
    //Generate the dynamic message
    val msgDef = MessageDefinition.newBuilder(dynamicMessage.messageName)
    //Add arguments to the dynamicMessage
    dynamicMessage.messageContent.foreach{
      arg =>
        msgDef.addField(
          arg.label,
          arg.argType,
          arg.argName,
          1
        )
    }
    //Generate the message fields
    val msg = msgDef.build()
    schemaBuilder.addMessageDefinition(msgDef.build())
    val schema = schemaBuilder.build()
    println(schema)








