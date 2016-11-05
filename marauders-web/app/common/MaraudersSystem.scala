package common

object MaraudersSystem {

  val host      = "172.22.5.79"
  val flumePort = 5001
  val mongoPort = 27017
  val urlMongo  = host + ":" + mongoPort
  val hdfsPort  = 9000
  val urlHDFS   = s"hdfs://$host:$hdfsPort"

}