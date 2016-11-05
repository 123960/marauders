package br.eisa.spark

import akka.actor.Actor
import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.flume._
import com.github.nscala_time.time.Imports._

import br.eisa.common._
import br.eisa.model._
import br.eisa.dispatcher.Dispatcher

object SparkFlumeStreaming extends SparkExecution {

  def start(implicit ssc: StreamingContext) = onLogTuples(dispatch)

  def dispatch(i: Incident) = MaraudersSystem.dispatcherActor ! Message(i)

  def onLogTuples(f: (Incident) => Unit)(implicit ssc: StreamingContext) = {

    val c: PartialFunction[(Option[String], Option[String], Option[String]), Incident] = { case (Some(d), Some(e), Some(a)) => new Incident(formatDate(d), e.replace("[", "").replace("]", ""), a.replaceAll("#", "")) }
    val logDateRegex    = "... \\d\\d".r
    val logErrorRegex   = "\\[\\d*\\]".r
    val logAccountRegex = "#\\d*#".r

    val bdlogStream  = FlumeUtils.createStream(ssc,
                                               MaraudersSystem.host,
                                               MaraudersSystem.flumePort)

    val warnLogs     = bdlogStream.map(flume => new String(flume.event.getBody.array))
                                  .filter(line => line contains "[WARN]")

    val logTuples    = warnLogs.map(line => {
                                      val logDate    = logDateRegex.findFirstIn(line)
                                      val logError   = logErrorRegex.findFirstIn(line)
                                      val logAccount = logAccountRegex.findFirstIn(line)
                                      (logDate, logError, logAccount)
                                    })
                               .filter(c.isDefinedAt _).map(c) //substitute of collect

    logTuples.foreachRDD { rdd => rdd.foreach(f) }
  }

  private def formatDate(str: String) = {
    val d  = "\\d\\d".r
    val m  = "...".r
    val mm = Map("Jan" -> "01", "Feb" -> "02",
                 "Mar" -> "03", "Apr" -> "04",
                 "May" -> "05", "Jun" -> "06",
                 "Jul" -> "07", "Aug" -> "08",
                 "Sep" -> "09", "Oct" -> "10",
                 "Nov" -> "11", "Dec" -> "12")
    d.findFirstIn(str).get + "/" + mm(m.findFirstIn(str).get) + "/" + DateTime.now.getYear
  }

}