package Yaml

import HelperUtils.CreateLogger
import org.yaml.snakeyaml.Yaml

import scala.io.Source
import scala.collection.JavaConverters._
import scala.collection.mutable

class YamlReader

object YamlReader extends App{
  //Init the logger
  val logger = CreateLogger(classOf[YamlReader])

  readYamlFile("example1.yaml")
  def readYamlFile(fileName : String): mutable.Map[String, Any] = {
    //logger.info("[readYamlFile]: Function invoked...")
    //Get the file with the YAML config from the resources
    //logger.info(s"[readYamlFile]: Reading the file $fileName...")
    val file = Source.fromResource(fileName).mkString
    //Read the file through SnakeYaml and convert it into a Map
    val yaml = new Yaml()
    val data = yaml.load(file.stripMargin).asInstanceOf[java.util.Map[String, Any]].asScala
    //Return the data
    //logger.info("[readYamlFile]: Return the data...")
    data
  }
}


