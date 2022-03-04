import NebulaScala2.Compiler.ToolboxGenerator
import akka.actor.Props
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
        |import akka.actor._
        |import com.github.os72.protobuf.dynamic.{DynamicSchema, MessageDefinition}
        |import com.google.protobuf.Descriptors.{Descriptor, FieldDescriptor}
        |import com.google.protobuf.DynamicMessage
        |class SimpleActor extends Actor {
        |override def receive: Receive = {
        | case test =>
        |    if (test.isInstanceOf[String]) {
        |        println("THERE WE GO!!!")
        |      }
        |  }
        |}
        |object SimpleActor {
        |def props() : Props = Props(new SimpleActor())
        |def ciao() : Unit = { val aaa: DynamicMessage = None }
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
    toolbox.compile(tree)().asInstanceOf[Props]
  }
  
}

