package br.eisa.spark

import org.apache.spark.streaming._

trait SparkExecution {

  def start(implicit ssc: StreamingContext): Unit
  
}