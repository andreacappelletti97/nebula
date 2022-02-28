package NebulaScala3.Parser

import NebulaScala3.Schema.{ActorDynamicSchema, ActorSchema, ActorSystemSchema, CaseClassSchema, DynamicMessageSchema}
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.apache.commons.lang3.StringEscapeUtils

import java.io.FileInputStream

object JSONParser {
  private val mapper = new ObjectMapper().registerModule(DefaultScalaModule)

  /**
   * Loads a schema from a JSON file.
   */
  def getDynamicMessagesFromJson(fileName : String) : Array[DynamicMessageSchema] = {
    val inputStream = new FileInputStream(fileName)
    try {
      mapper.readValue(inputStream, classOf[Array[DynamicMessageSchema]])
    } finally {
      inputStream.close()
    }
  }

  /**
   * Loads a schema from a JSON file.
   */
  def getDynamicActorsFromJson(fileName : String) : Array[ActorDynamicSchema] = {
    val inputStream = new FileInputStream(fileName)
    try {
      mapper.readValue(inputStream, classOf[Array[ActorDynamicSchema]])
    } finally {
      inputStream.close()
    }
  }
  
  /**
   * Loads a schema from a JSON file.
   */
  def getActorSchemaFromJson(fileName: String): Array[ActorSchema] = {
    val inputStream = new FileInputStream(fileName)
    try {
      mapper.readValue(inputStream, classOf[Array[ActorSchema]])
    } finally {
      inputStream.close()
    }
  }

  /**
   * Loads a schema from a JSON file.
   */
  def getActorSystemFromJson(fileName: String): Array[ActorSystemSchema] = {
    val inputStream = new FileInputStream(fileName)
    try {
      mapper.readValue(inputStream, classOf[Array[ActorSystemSchema]])
    } finally {
      inputStream.close()
    }
  }

  /**
   * Loads a schema from a JSON file.
   */
  def getMessagesSchemaFromJson(fileName: String): Array[CaseClassSchema] = {
    val inputStream = new FileInputStream(fileName)
    try {
      mapper.readValue(inputStream, classOf[Array[CaseClassSchema]])
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

