package Nebula

import akka.actor.{Actor, ActorSystem, Props}
import com.github.os72.protobuf.dynamic.{DynamicSchema, MessageDefinition}
import com.google.protobuf.Descriptors.{Descriptor, FieldDescriptor}
import com.google.protobuf.DynamicMessage

class SimpleActor extends Actor {
  override def receive: Receive = {
    case dynamicMessage: DynamicMessage => {
      println(dynamicMessage)
      val title = dynamicMessage.getField(dynamicMessage.getDescriptorForType.findFieldByName("title"))
      val year = dynamicMessage.getField(dynamicMessage.getDescriptorForType.findFieldByName("year"))
      println("Film title is " + title)
      println("The film year is " + year)
    }
  }
}

class SimpleActorValues extends Actor {
  override def receive: Receive = {
    case dynamicMessage: DynamicMessage => {
      println(dynamicMessage.getAllFields.values())
    }
  }
}

object ProtoTest:
  @main def runDynamicProtoTest =
    // Create dynamic schema
    val schemaBuilder = DynamicSchema.newBuilder
    schemaBuilder.setName("FilmSchemaDynamic.proto")

    //Build a message definition
    val msgDef = MessageDefinition.newBuilder("Film")
      .addField("required", "string", "title", 1)
      .addField("required", "int32", "year", 2)
      .build()

    //Add message definition to the schema
    schemaBuilder.addMessageDefinition(msgDef)
    val schema = schemaBuilder.build
    //println(schema)

    val msgBuilder = schema.newMessageBuilder("Film")
    val msgDesc = msgBuilder.getDescriptorForType
    val msg = msgBuilder
      .setField(msgDesc.findFieldByName("title"), "Fight Club")
      .setField(msgDesc.findFieldByName("year"), 1997)

    val dynamicFilmMessage = msg.build()

    val system = ActorSystem("system")
    val simpleActor = system.actorOf(Props[SimpleActor](), "simpleActor")
    val simpleActorValues = system.actorOf(Props[SimpleActorValues](), "simpleActorValues")

    simpleActor ! dynamicFilmMessage
    simpleActorValues ! dynamicFilmMessage

    system.terminate()









