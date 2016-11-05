package report

import play.api.libs.iteratee.{Input, Enumeratee, Enumerator}
import reactivemongo.api._
import reactivemongo.bson._
import reactivemongo.core.commands._
import scala.concurrent.{Future, Await}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Try, Success, Failure}

import com.github.nscala_time.time.StaticDateTimeFormat
import com.github.nscala_time.time.Imports.DateTime

import common.MaraudersSystem._
import model._

object Report {

  lazy val driver = new MongoDriver
  lazy val con    = driver.connection(List(urlMongo))
  lazy val db     = con("marauders")
  lazy val coll   = db("client_error_by_day")

  implicit object CountIncidentsReader extends BSONDocumentReader[(Long, Incident)] {
    def read(doc: BSONDocument): (Long, Incident) = {
      val count   = doc.getAs[Long]("count").get
      val date    = doc.getAs[String]("date").get
      val error   = doc.getAs[String]("error").get
      val account = doc.getAs[String]("account").get      
      (count, Incident(date, error, account))
    }
  }

  def incidentsPerDay(n: Int): Enumerator[(Long, Incident)] = {
    val query = BSONDocument("count" -> BSONDocument("$gt" -> n))
    coll.find(query)
        .cursor[(Long, Incident)]()
        .enumerate()
  }

}