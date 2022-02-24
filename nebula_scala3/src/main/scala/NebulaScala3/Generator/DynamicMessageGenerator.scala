package NebulaScala3.Generator

import NebulaScala3.Schema.{DynamicMessageContentSchema, DynamicMessageSchema}
import com.github.os72.protobuf.dynamic.{DynamicSchema, MessageDefinition}
import com.google.protobuf.DynamicMessage

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
    val list = generateDynamicMessages(Array(dynamicMessageSchema), 0, Seq.empty)
    println(list)

  def generateDynamicMessages(dynamicMessageList: Array[DynamicMessageSchema],
                              iterator : Int,
                              dynamicMessage : Seq[DynamicSchema]
                             ) : Seq[DynamicSchema] =
    if(iterator >= dynamicMessageList.length) dynamicMessage
    else generateDynamicMessages(
      dynamicMessageList, iterator + 1, dynamicMessage :+
        generateMessage(dynamicMessageList(iterator))
    )

  private def generateMessage(dynamicMessage : DynamicMessageSchema) : DynamicSchema =
    val schemaBuilder = DynamicSchema.newBuilder
    //schemaBuilder.setName("PersonSchemaDynamic.proto")
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
    schema

    // Create dynamic message from schema
    //val msgBuilder = schema.newMessageBuilder("Person")
    /*
    val msgDesc = msgBuilder.getDescriptorForType
    val msgInstance =
      msgBuilder.setField(msgDesc.findFieldByName("name"), "Andrea")
        .setField(msgDesc.findFieldByName("surname"), "Hats")
        .build
     */
    //println(msgInstance)
    //msgBuilder








