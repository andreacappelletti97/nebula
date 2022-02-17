import NebulaScala2.Compiler.ToolboxGenerator

import collection.mutable.Stack
import org.scalatest.flatspec.AnyFlatSpec

class ActorCodeCompilerTest extends AnyFlatSpec {

  "The toolbox" should "compile and instantiate an Actor" in {
    val toolbox = ToolboxGenerator.generateToolbox()
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
      val tree = toolbox.parse(actorCode)
  }
  
}

