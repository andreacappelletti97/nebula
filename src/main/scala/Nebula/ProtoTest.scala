package Nebula

import akka.actor.{Actor, ActorSystem, Props}
import com.github.os72.protobuf.dynamic.{DynamicSchema, MessageDefinition}
import com.google.protobuf.Descriptors.{Descriptor, FieldDescriptor}
import com.google.protobuf.DynamicMessage

class SimpleActor extends Actor {
  override def receive: Receive = {
    //TODO: decompose inside the actor message the dynamic Proto!

    case dynamicMessage => {
      if (dynamicMessage.isInstanceOf[DynamicMessage]) {
        println("THERE WE GO!!!")
      }
  /*
      dynamicMessage.getDescriptorForType.getName match {
        case "Film" => {
          println(dynamicMessage)
          val title = dynamicMessage.getField(dynamicMessage.getDescriptorForType.findFieldByName("title"))
          val year = dynamicMessage.getField(dynamicMessage.getDescriptorForType.findFieldByName("year"))
          println("Film title is " + title)
          println("The film year is " + year)
*/
    }

        //case _  => println("Invalid month")  // the default, catch-all
      }
    }


class SimpleActorValues extends Actor {
  val myMap = Set("title", "year")
  override def receive: Receive = {
    case dynamicMessage: DynamicMessage => {
      dynamicMessage.getDescriptorForType.findFieldByName("title")
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
    //simpleActorValues ! dynamicFilmMessage

    println("NAME!!! " + dynamicFilmMessage.getDescriptorForType.getName)

    
    //val myMap = dynamicFilmMessage.getAllFields
    //println(myMap.keySet())


    system.terminate()









