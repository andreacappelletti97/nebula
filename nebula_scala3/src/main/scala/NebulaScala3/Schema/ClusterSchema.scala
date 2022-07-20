package NebulaScala3.Schema

case class ClusterSchema(
                                transport : String,
                                hostname: String,
                                port: Int,
                                seedNodes: Seq[String],
                                initPorts: List[Int]
                                )
