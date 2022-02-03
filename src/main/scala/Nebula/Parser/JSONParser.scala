package Nebula.Parser

import Nebula.Schema.ActorSchema
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.apache.commons.lang3.StringEscapeUtils

import java.io.FileInputStream

object JSONParser {
  private val mapper = new ObjectMapper().registerModule(DefaultScalaModule)

  /**
   * Loads a schema from a JSON file.
   */
  def fromJson(fileName: String): Array[Any] = {
    val inputStream = new FileInputStream(fileName)
    try {
      mapper.readValue(inputStream, classOf[Array[Any]])
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

