package br.eisa.persist

import akka.actor.Actor
import akka.actor.Props
import reactivemongo.api._
import reactivemongo.bson._
import reactivemongo.core.commands._
import reactivemongo.api.collections.bson.BSONCollection

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Try, Success, Failure}

import com.github.nscala_time.time.Imports._

import br.eisa.common._
import br.eisa.model._

class ErrorMapPersist extends Actor {

  lazy val driver = new MongoDriver
  lazy val con    = driver.connection(List(MaraudersSystem.urlMongo))
  lazy val db     = con("marauders")
  lazy val coll   = db("error_map")

  implicit object ErrorMapElementReader extends BSONDocumentReader[ErrorMapElement] {
    def read(doc: BSONDocument): ErrorMapElement = {
      val date  = doc.getAs[String]("date").get
      val error = doc.getAs[String]("error").get
      val count = doc.getAs[Int]("count").get
      ErrorMapElement(date, error, count)
    }
  }

  def receive = {
    case msg: IncidentMessage => upsert(msg)
    case "ErrorMapFullReport" => errorMapFullReport.onComplete(sender ! _)
    case _                    => println("huh?")
  }

  def upsert(msg: IncidentMessage) = {
    val query    = BSONDocument("date"  -> msg.inc.date, "error" -> msg.inc.error)
    val modifier = BSONDocument("$inc"  -> BSONDocument("count" -> 1), 
                                "$push" -> BSONDocument("accounts" -> msg.inc.account))

    coll.update(query, modifier, upsert = true).onComplete {
      case Success(d)  => println("OK-Update")
      case Failure(ex) => println("Error on update")
    }
  }

  def errorMapFullReport = {
    val query     = BSONDocument("date" -> BSONDocument("$regex" -> lastNdaysFindCondition(30)))
    val filter    = BSONDocument("date" -> 1, "error" -> 1, "count" -> 1)
    coll.find(query, filter).cursor[ErrorMapElement].collect[List]()
  }

  private def lastNdaysFindCondition(n: Int): String = {
    val ds = dateRange(DateTime.now.minusDays(n), DateTime.now, Period.days(1))
    val ss = ds.map(d => ("00" + d.getDayOfMonth takeRight 2) + "\\/" + d.getMonthOfYear + "\\/" + d.getYear)
               .mkString("|")
    "/" + ss + "/"
  }

  private def dateRange(start: DateTime, end: DateTime, step: Period): Iterator[DateTime] =
    Iterator.iterate(start)(_.plus(step)).takeWhile(!_.isAfter(end))

}