package br.eisa.common

import br.eisa.model._

object Message {
   def apply (msg: (String, String, String)) = new TupleMessage(msg)
   def apply (i: Incident)                   = new IncidentMessage(i)
}

case class TupleMessage(val msg: (String, String, String))
case class IncidentMessage(val inc: Incident)