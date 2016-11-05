package controllers

import play.api._
import play.api.mvc._
import play.api.libs.EventSource
import streams.AvailableStreams

class Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def incidentsByClientPerDay = Action {
    Ok.chunked(AvailableStreams.incidentsByClientPerDay &> EventSource()).as(EVENT_STREAM)
  }

}