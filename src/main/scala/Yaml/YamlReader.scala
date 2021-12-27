package Yaml

import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor

import java.io.{File, FileInputStream, InputStream}
import scala.beans.BeanProperty
import scala.io.Source
import scala.collection.JavaConverters._

object YamlReader extends App{
  val file = Source.fromResource("config.yaml").getLines()
  val yaml = new Yaml()
  val data = yaml.load(
    """
      |accountName: Ymail Account
      |username: johndoe
      |password: secret
      |mailbox: INBOX
      |imapServerUrl: imap.mail.yahoo.com
      |protocol: imaps
      |minutesBetweenChecks: 1
      |usersOfInterest: [barney, betty, wilma]
      """.stripMargin).asInstanceOf[java.util.Map[String, Any]].asScala
  println(data)
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
