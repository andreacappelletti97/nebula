package NebulaScala3.Schema

case class CustomObjectSchema(className:String,
                              classType: String,
                              executionCode: String,
                              endpointSchema: ExternalEndpointSchema,
                              transitions: Seq[String])
