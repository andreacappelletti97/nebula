package TreeHugger.Schema

import java.io.FileInputStream

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.apache.commons.lang3.StringEscapeUtils

case class ActorSchema(actorName: TypeName, comment: String, fields: Seq[Field])

object ActorSchema {

  private val mapper = new ObjectMapper().registerModule(DefaultScalaModule)

  /**
   * Loads a schema from a JSON file.
   */
  def fromJson(fileName: String): Array[ActorSchema] = {
    val inputStream = new FileInputStream(fileName)
    try {
      mapper.readValue(inputStream, classOf[Array[ActorSchema]])
    } finally {
      inputStream.close()
    }
  }

  /**
   * Converts a schema to JSON.
   */
  def toJson(schema: ActorSchema): String = {
    mapper.writeValueAsString(schema)
  }

  /**
   * Converts a schema to JSON and escapes it according to Java/Scala rules.
   */
  def toEscapedJson(schema: ActorSchema): String = {
    val json = toJson(schema)
    StringEscapeUtils.escapeJava(json)
  }

}
