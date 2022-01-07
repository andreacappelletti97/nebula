package Week1

import HelperUtils.{CreateLogger, ObtainConfigReference}
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

  case class MyActor(@JsonProperty("Name") myName: String, @JsonProperty("Case") myCases : Seq[String], @JsonProperty("Code") myCode : Seq[String])

  def composeActors(configuration : Any) : Seq[MyActor] = {
    val mySeq : Seq[MyActor] = Seq.empty[MyActor]
    val myArray = configuration.asInstanceOf[util.ArrayList[Object]]
    //Create Gson
    val gson = new Gson
    val mapper = new ObjectMapper()
    mapper.registerModule(DefaultScalaModule)
    myArray.forEach(c => {
      val myjson = gson.toJson(c, classOf[util.LinkedHashMap[Any, Any]])
      println(myjson)
      val data = mapper.readValue(myjson.stripMargin, classOf[MyActor])
      println(data)
    })
    mySeq
  }
}

