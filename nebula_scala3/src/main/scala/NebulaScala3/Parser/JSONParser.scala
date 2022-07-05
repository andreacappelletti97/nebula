package NebulaScala3.Parser

import NebulaScala3.Schema.{ActorSchema, CinnamonMonitoringSchema, ClusterSchema, MessageSchema, OrchestratorSchema}
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.apache.commons.lang3.StringEscapeUtils

import java.io.FileInputStream

object JSONParser {
  private val mapper = new ObjectMapper().registerModule(DefaultScalaModule)

  def getMonitoringFromJson(fileName : String) : CinnamonMonitoringSchema = {
    val inputStream = new FileInputStream(fileName)
    try {
      mapper.readValue(inputStream, classOf[CinnamonMonitoringSchema])
    } finally {
      inputStream.close()
    }
  }
  
  
  def getOrchestratorFromJson(fileName : String) : Array[OrchestratorSchema] = {
    val inputStream = new FileInputStream(fileName)
    try {
      mapper.readValue(inputStream, classOf[Array[OrchestratorSchema]])
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


  def getClusterShardingSchemaFromJson(fileName: String): ClusterSchema = {
    val inputStream = new FileInputStream(fileName)
    try {
      mapper.readValue(inputStream, classOf[ClusterSchema])
    } finally {
      inputStream.close()
    }
  }


  /**
   * Loads a schema from a JSON file.
   */
  def getMessagesSchemaFromJson(fileName: String): Array[MessageSchema] = {
    val inputStream = new FileInputStream(fileName)
    try {
      mapper.readValue(inputStream, classOf[Array[MessageSchema]])
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

