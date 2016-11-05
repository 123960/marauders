package br.eisa.spark

import org.apache.spark._
import org.apache.spark.streaming._
import br.eisa.common._

object SparkMainStreaming {

  val executions = Map("marauders-flume" -> SparkFlumeStreaming)

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("spark://sarin:7077")
                              .setAppName(args(0))
                              .set("deploy-mode", "cluster")
    implicit val ssc  = new StreamingContext(conf, Seconds(10))

    executions(args(0)).start

    ssc.start
    ssc.awaitTermination

  }

}