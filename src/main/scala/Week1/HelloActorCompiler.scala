package Week1

import HelperUtils.{CreateLogger, ObtainConfigReference}
import akka.actor.Props
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.google.gson.Gson
import java.util
import scala.reflect.runtime.currentMirror
import scala.tools.reflect.ToolBox

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


  case class MyActor(@JsonProperty("Name") myName: String, @JsonProperty("Case") myCases : Set[String], @JsonProperty("Code") myCode : Set[String])

  def composeActors(configuration : Any) : Set[Props] = {
    val myArray = configuration.asInstanceOf[util.ArrayList[Object]]
    println(myArray)
    myArray.forEach(c => println(c))
    val mapper = new ObjectMapper()
    mapper.registerModule(DefaultScalaModule)
    val json: String =
      """
        |{
        | "Name": "ciao",
        | "Case": ["me"],
        | "Code": ["awesome"]
        |}
      """.stripMargin

    val gson = new Gson
    val myjson = gson.toJson(myArray.get(0), classOf[util.LinkedHashMap[Any, Any]])
    println(myjson)
    val data = mapper.readValue(myjson.stripMargin, classOf[MyActor])
    println(data)

    Set()
  }
}

