package Yaml

import org.yaml.snakeyaml.Yaml

import scala.beans.BeanProperty
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
      |Actor:
      | id: 1
      | name: HelloActor1
      """.stripMargin).asInstanceOf[java.util.Map[String, Any]].asScala
  println(data)
  println(data.get("Actor"))
}

class EmailAccount {
  @BeanProperty var accountName = ""
  @BeanProperty var username = ""
  @BeanProperty var password = ""
  @BeanProperty var mailbox = ""
  @BeanProperty var imapServerUrl = ""
  @BeanProperty var protocol = ""
  @BeanProperty var minutesBetweenChecks = 0
  @BeanProperty var usersOfInterest = new java.util.ArrayList[String]()
  override def toString: String = s"acct: $accountName, user: $username, url: $imapServerUrl"
}
