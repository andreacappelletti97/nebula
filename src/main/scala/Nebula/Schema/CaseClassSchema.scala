package Nebula.Schema

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

import java.io.FileInputStream

/***
 * A case object is defined by a name without any argument
 * A case class is defined by a name and some arguments
 * Arguments are defined as @ArgumentSchema, that contains argName and argType
 ***/
case class CaseClassSchema(caseClassName: String, caseClassArgs: Seq[ArgumentSchema])

object CaseClassSchema {
  private val mapper = new ObjectMapper().registerModule(DefaultScalaModule)

  /**
   * Loads a schema from a JSON file.
   */
  def fromJson(fileName: String): Array[CaseClassSchema] = {
    val inputStream = new FileInputStream(fileName)
    try {
      mapper.readValue(inputStream, classOf[Array[CaseClassSchema]])
    } finally {
      inputStream.close()
    }
  }


}