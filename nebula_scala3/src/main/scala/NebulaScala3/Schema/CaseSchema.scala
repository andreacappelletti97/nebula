package NebulaScala3.Schema

case class CaseSchema(className:String,
                      executionCode: String,
                      endpointSchema: ExternalEndpointSchema,
                      transitions: Seq[String])
