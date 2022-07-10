package NebulaScala3.Schema

import java.util

case class ExternalEndpointSchema(url: String,
                                  className: String,
                                  methodName: String,
                                  param: util.HashMap[String, Object],
                                  methodType: Any)
