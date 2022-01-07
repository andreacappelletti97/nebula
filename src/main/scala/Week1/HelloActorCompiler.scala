package Week1

import HelperUtils.{CreateLogger, ObtainConfigReference}
import akka.actor.Props
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

import java.util
import scala.reflect.runtime.currentMirror
import scala.tools.reflect.ToolBox
import scala.collection.JavaConverters._

class HelloActorCompiler
object HelloActorCompiler {
  //Init the config file to get static params
  val config = ObtainConfigReference("helloActor") match {
    case Some(value) => value
    case None => throw new RuntimeException("Cannot obtain a reference to the config data.")
  }
  //Init the logger
  val logger = CreateLogger(classOf[HelloActorCompiler])

  //Create toolbox
  val toolbox = currentMirror.mkToolBox()

  def composeActors(configuration : Any) : Set[Props] = {
    val myArray = configuration.asInstanceOf[util.ArrayList[Object]]
    println(myArray)
    val mapper = new ObjectMapper()
    mapper.registerModule(DefaultScalaModule)
    val json: String =
      """
        |{
        | "Name": "ciao",
        | "Case": "me",
        | "Code": "awesome"
        |}
      """.stripMargin
    val data = mapper.readValue(json, classOf[MyActor])
    println(data)

    Set()
  }
}

case class MyActor(@JsonProperty("Name") myName: String, @JsonProperty("Case") myCases : Array[String], @JsonProperty("Code") myCode : Array[String])