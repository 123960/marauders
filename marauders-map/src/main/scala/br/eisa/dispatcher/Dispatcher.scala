package br.eisa.dispatcher

import akka.actor.Actor

import br.eisa.common._
import br.eisa.persist.ErrorMapPersist

class Dispatcher extends Actor {

  val receivers = List(MaraudersSystem.errorMapPersistActor)

  def receive = {
    case msg: IncidentMessage => receivers foreach (_ forward msg)
    case "ErrorMapFullReport" => MaraudersSystem.errorMapPersistActor forward "ErrorMapFullReport"
    case _                    => println("huh?")
  }

}