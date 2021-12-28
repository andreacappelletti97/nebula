package Yaml

import org.yaml.snakeyaml.Yaml
import scala.io.Source
import scala.collection.JavaConverters._

class YamlReader

object YamlReader extends App{
  val file = Source.fromResource("config.yaml").getLines()
  val yaml = new Yaml()
  val data = yaml.load(
    """
      |Actor:
      | id: 0
      | name: HelloActor0
      """.stripMargin).asInstanceOf[java.util.Map[String, Any]].asScala
  println(data)
  println(data.get("Actor"))
}


