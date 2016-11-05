package br.eisa.common

import akka.actor.ActorSystem
import akka.actor.Props

import br.eisa.dispatcher.Dispatcher
import br.eisa.persist.ErrorMapPersist

object MaraudersSystem {

  val system               = ActorSystem("MaraudersSystem") 
  val dispatcherActor      = system.actorOf(Props[Dispatcher],      name = "dispatcher")
  val errorMapPersistActor = system.actorOf(Props[ErrorMapPersist], name = "error-map-persist")

  val dispatcherActorProps  = Props[Dispatcher]
  val errorMapPersistProps  = Props[ErrorMapPersist]
  
  val host            = "sarin"
  val flumePort       = 5001
  val mongoPort       = 27017
  val urlMongo        = host + ":" + mongoPort

}