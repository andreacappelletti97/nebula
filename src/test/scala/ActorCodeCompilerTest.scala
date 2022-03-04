import Nebula.SimpleActor
import NebulaScala2.Compiler.ToolboxGenerator
import akka.actor.{ActorSystem, Props}
import com.github.os72.protobuf.dynamic.{DynamicSchema, MessageDefinition}
import com.google.protobuf.DynamicMessage

import collection.mutable.Stack
import org.scalatest.flatspec.AnyFlatSpec

class ActorCodeCompilerTest extends AnyFlatSpec {

  "The toolbox" should "compile and instantiate an Actor" in {

    val toolbox = ToolboxGenerator.generateToolbox()
    /*
    val actorCode : String = """
        |  import akka.actor._
        |  class HelloActor extends Actor {
        |    override def receive: Receive = {
        |       case _ => println("Hello from HelloActor!")
        |    }
        |  }
        |  object HelloActor {
        |    def props() : Props = Props(new HelloActor())
        |  }
        |
        |
        |  return HelloActor.props()
        |""".stripMargin
    */
    
    val actoreCode2 =
      """
        |import com.google.protobuf.DynamicMessage
        |import akka.actor._
        |import com.github.os72.protobuf.dynamic.{DynamicSchema, MessageDefinition}
        |import com.google.protobuf.Descriptors.{Descriptor, FieldDescriptor}
        |import SomeExternalImport._
        |class SimpleActor extends Actor {
        |override def receive: Receive = {
        | case test => {
        |   println("Calling an exernal function to process test")
        |   ciao(test)
        |    }
        |  }
        |}
        |object SimpleActor {
        |def props() : Props = Props(new SimpleActor())
        |}
        |
        |
        |return SimpleActor.props()
        |""".stripMargin

    /*
    val actorCode =
      """
        |import akka.actor._
        |          import com.google.protobuf.DynamicMessage
        |      class SimpleActor extends Actor {
        |      override def receive: Receive = {
        |      case dynamicMessage: DynamicMessage => {
        |      dynamicMessage.getDescriptorForType.getName match {
        |
        |case "Authentication" => {
        |println("hello!")
        |}
        |
        |      }
        |      }
        |      }
        |      }
        |
        |object SimpleActor {
        |def props() : Props = Props(new SimpleActor())
        |}
        |
        |
        |return SimpleActor.props()
        |""".stripMargin
    */

/*
    val actoreCode2 =
      """
        |import akka.actor._
        |class SimpleActor extends Actor {
        |override def receive: Receive = {
        | case NebulaScala3.Scala3Main.obj => println("Dynamic message received!")
        | case _  => println("Whatever!")  // the default, catch-all
        |  }
        |}
        |object SimpleActor {
        |def props() : Props = Props(new SimpleActor())
        |}
        |return SimpleActor.props()
        |""".stripMargin
*/
    val tree = toolbox.parse(actoreCode2)
    val props = toolbox.compile(tree)().asInstanceOf[Props]

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
    val simpleActor = system.actorOf(props, "simpleActor")
    simpleActor ! dynamicFilmMessage


  }
  
}

