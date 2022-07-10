package NebulaScala3.Schema

import java.util

case class ExternalEndpointSchema(url: String,
                                  className: String,
                                  methodName: String,
                                  param: ArgumentSchema,
                                  methodType: Any)
