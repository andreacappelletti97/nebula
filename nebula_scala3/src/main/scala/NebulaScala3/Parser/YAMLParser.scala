package NebulaScala3.Parser

import NebulaScala3.Schema.{ActorSchema, MessageSchema}
import org.yaml.snakeyaml.Yaml
import java.io.{File, FileInputStream}

object YAMLParser:
 val yaml = new Yaml

 def getActorSchemaFromYaml(fileName: String) : Array[Any] =
  val input = new FileInputStream(new File(fileName))
  val actorsList : java.util.ArrayList[ActorSchema] = yaml.load(input).asInstanceOf[java.util.ArrayList[ActorSchema]]
  actorsList.toArray

 def getMessagesSchemaFromYaml(fileName: String) : Array[Any] =
  val input = new FileInputStream(new File(fileName))
  val messagesList : java.util.ArrayList[MessageSchema] = yaml.load(input).asInstanceOf[java.util.ArrayList[MessageSchema]]
  messagesList.toArray






