package streams

import play.api.libs.iteratee.{Input, Enumeratee, Enumerator}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.libs.functional.syntax._
import scala.util.{Success, Failure}

import report._
import model._

object AvailableStreams {
  import sys.process._

  case class CountIncident(count: Long, date: String, error: String, account: String)

  implicit val CountIncidentWrites: Writes[CountIncident] = (
      (JsPath \ "count").write[Long] and
      (JsPath \ "date").write[String] and
      (JsPath \ "error").write[String] and
      (JsPath \ "account").write[String]
    )(unlift(CountIncident.unapply))

  def incidentsByClientPerDay: Enumerator[JsValue] = 
      Report.incidentsPerDay(n = 2)
            .map(e => Json.toJson(CountIncident(e._1, e._2.date, e._2.error, e._2.account)))
/*  {

    Enumerator.generateM ({
      Report.incidentsPerDay(2)
            .transform(l => Some(l.map(t => Json.toJson(CountIncident(t._1, t._2.date, t._2.error, t._2.account)))
                             .foldLeft(Json.obj())((acc, j) => acc ++ j.as[JsObject])),
                       f => f)
    })
  }*/

}