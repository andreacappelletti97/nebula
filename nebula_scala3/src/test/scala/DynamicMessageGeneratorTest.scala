import NebulaScala3.Generator.DynamicMessageGenerator.generateDynamicMessages
import NebulaScala3.Parser.JSONParser
import NebulaScala3.Schema.{DynamicMessageContentSchema, DynamicMessageSchema}
import org.scalatest.flatspec.AnyFlatSpec

class DynamicMessageGeneratorTest extends AnyFlatSpec {
  "The dynamic message generator" should "generate Person message" in {
    val dynamicMessageSchema : DynamicMessageSchema = DynamicMessageSchema(
      "Authentication",
      Seq(DynamicMessageContentSchema(
        "required",
        "email",
        "string"
      ),
        DynamicMessageContentSchema(
          "required",
          "password",
          "string"
        )
      )
    )
    val testList = generateDynamicMessages(Array(dynamicMessageSchema), 0, Seq.empty)
    val dynamicMessageJson =  JSONParser.getDynamicMessagesFromJson("src/main/resources/json/DynamicMessages.json")

    val jsonList = generateDynamicMessages(dynamicMessageJson, 0, Seq.empty)
    //assert(testList.equals(jsonList))


  }
}
