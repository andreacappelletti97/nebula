package NebulaScala3

import com.github.os72.protobuf.dynamic.DynamicSchema

object ProtoExample extends App {
  val schemaBuilder = DynamicSchema.newBuilder

  import com.github.os72.protobuf.dynamic.{DynamicSchema, MessageDefinition}

  schemaBuilder.setName("PersonSchemaDynamic.proto")

  val msgDef = MessageDefinition.newBuilder("Person") // message Person
    .addField("required", "int32", "id", 1) // required int32 id = 1
    .addField("required", "string", "name", 2) // required string name = 2
    .addField("optional", "string", "email", 3) // optional string email = 3
    .build();


  schemaBuilder.addMessageDefinition(msgDef)
  val schema = schemaBuilder.build

  // Create dynamic message from schema
  val msgBuilder = schema.newMessageBuilder("Person")
  val msgDesc = msgBuilder.getDescriptorForType
  val msg = msgBuilder.setField(msgDesc.findFieldByName("id"), 1).setField(msgDesc.findFieldByName("name"), "Alan Turing").setField(msgDesc.findFieldByName("email"), "at@sis.gov.uk").build
  println(msg)


}
