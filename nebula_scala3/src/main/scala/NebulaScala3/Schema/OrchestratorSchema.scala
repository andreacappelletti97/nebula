package NebulaScala3.Schema


case class OrchestratorSchema(name: String,
                              transitions : Seq[String],
                              initMessages : Seq[String],
                              numOfInstance : Int
                             )
