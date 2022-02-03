package Yaml

import org.yaml.snakeyaml.Yaml

import scala.io.Source
import scala.collection.JavaConverters._
import scala.collection.mutable
import com.google.gson.Gson
import com.google.gson.GsonBuilder

import java.util

class YamlReader

object YamlReader extends App{
  val yaml = new Yaml()
  println(yamlToJsonConverter("TreeHugger.yaml"))


  def readYamlFile(fileName : String): mutable.Map[String, Any] = {
    //logger.info("[readYamlFile]: Function invoked...")
    //Get the file with the YAML config from the resources
    //logger.info(s"[readYamlFile]: Reading the file $fileName...")
    val file = Source.fromResource(fileName).mkString
    //Read the file through SnakeYaml and convert it into a Map
    val data = yaml.load(file.stripMargin).asInstanceOf[java.util.Map[String, Any]].asScala
    //Return the data
    //logger.info("[readYamlFile]: Return the data...")
    data
  }

  def yamlToJsonConverter(fileName: String): String = {
    val file = Source.fromResource(fileName).mkString
    val data = yaml.load(file.stripMargin).asInstanceOf[java.util.Map[String, Any]].asScala
    val gson = new GsonBuilder().setPrettyPrinting.create
    val json = gson.toJson(data,classOf[mutable.LinkedHashMap[String, Any]])
    json
  }


}


