package TreeHugger.Schema

import java.io.FileInputStream

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.apache.commons.lang3.StringEscapeUtils

case class ActorTemplate(actorName: String, actorArgs: Seq[ArgumentTemplate], methods: Seq[MethodTemplate])

object ActorTemplate {

  private val mapper = new ObjectMapper().registerModule(DefaultScalaModule)

  /**
   * Loads a schema from a JSON file.
   */
  def fromJson(fileName: String): Array[ActorTemplate] = {
    val inputStream = new FileInputStream(fileName)
    try {
      mapper.readValue(inputStream, classOf[Array[ActorTemplate]])
    } finally {
      inputStream.close()
    }
  }

  /**
   * Converts a schema to JSON.
   */
  def toJson(schema: ActorTemplate): String = {
    mapper.writeValueAsString(schema)
  }

  /**
   * Converts a schema to JSON and escapes it according to Java/Scala rules.
   */
  def toEscapedJson(schema: ActorTemplate): String = {
    val json = toJson(schema)
    StringEscapeUtils.escapeJava(json)
  }

}
