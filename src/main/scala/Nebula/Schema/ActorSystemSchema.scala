package Nebula.Schema

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

import java.io.FileInputStream


case class ActorSystemSchema(actorSystemName: String, actorsInstances: Seq[Int])

object ActorSystemSchema {
  private val mapper = new ObjectMapper().registerModule(DefaultScalaModule)

  /**
   * Loads a schema from a JSON file.
   */
  def fromJson(fileName: String): Array[ActorSystemSchema] = {
    val inputStream = new FileInputStream(fileName)
    try {
      mapper.readValue(inputStream, classOf[Array[ActorSystemSchema]])
    } finally {
      inputStream.close()
    }
  }


}
