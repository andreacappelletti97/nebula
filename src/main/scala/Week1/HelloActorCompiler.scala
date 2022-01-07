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

  val gson = new Gson

  val mapper = new ObjectMapper()
  mapper.registerModule(DefaultScalaModule)

  case class MyActor(@JsonProperty("Name") myName: String, @JsonProperty("Case") myCases : Seq[String], @JsonProperty("Code") myCode : Seq[String])

  /* This recursive function dynamically creates the MyActor objects based on the application config */
  def populateActors(actorsList : Seq[MyActor],n : Integer, myArray : util.ArrayList[Object], x: Integer) : Seq[MyActor] = {
    if(n==0) actorsList
    else {
      val myjson = gson.toJson(myArray.get(x), classOf[util.LinkedHashMap[Any, Any]])
      val data = mapper.readValue(myjson.stripMargin, classOf[MyActor])
      populateActors(actorsList :+ data, n - 1, myArray, x + 1)
    }
    }

  def composeActors(configuration : Any) : Seq[MyActor] = {
    val myArray = configuration.asInstanceOf[util.ArrayList[Object]]
    //Create Gson
    val newActorsList: Seq[MyActor] = Seq.empty[MyActor]
    val actorList : Seq[MyActor] = populateActors(newActorsList, 2, myArray, 0)
    println(actorList)
    actorList
  }
}

