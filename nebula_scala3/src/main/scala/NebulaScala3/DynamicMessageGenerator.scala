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
      ),
        DynamicMessageContentSchema(
          "required",
          "surname",
          "string"
        )
      )
    )
    generateMessage(dynamicMessageSchema)


  def generateMessage(dynamicMessage : DynamicMessageSchema) =
    val schemaBuilder = DynamicSchema.newBuilder
    schemaBuilder.setName("PersonSchemaDynamic.proto")
    //Generate the dynamic message
    val msgDef = MessageDefinition.newBuilder(dynamicMessage.messageName)
    //Add arguments to the dynamicMessage
    dynamicMessage.messageContent.zipWithIndex.foreach{
      case(content, index) =>
        msgDef.addField(
          content.label,
          content.argType,
          content.argName,
          index + 1
        )
    }
    //Generate the message fields
    val msg = msgDef.build()
    schemaBuilder.addMessageDefinition(msgDef.build())
    val schema = schemaBuilder.build()
    println(schema)

    // Create dynamic message from schema
    val msgBuilder = schema.newMessageBuilder("Person")
    val msgDesc = msgBuilder.getDescriptorForType
    val msgInstance =
      msgBuilder.setField(msgDesc.findFieldByName("name"), "Andrea")
        .setField(msgDesc.findFieldByName("surname"), "Hats")
        .build

    println(msgInstance)








