package Nebula

import NebulaScala2.Persistence.Cassandra

object CassandraInit:
  @main def runCassandra: Unit =
    Cassandra.runCassandra()
