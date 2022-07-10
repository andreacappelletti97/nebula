package NebulaScala3.Schema

case class CaseSchema(className: String,
                      predefinedStatement: String,
                      executionCode: String,
                      endpointSchema: ExternalEndpointSchema,
                      transitions: Seq[String])
