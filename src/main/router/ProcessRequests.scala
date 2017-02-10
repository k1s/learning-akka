package router

import akka.actor.{Actor, ActorRef, Props}
import messages._

/**
  *
  */
class ProcessRequests(storage: ActorRef) extends Actor {

  def receive = {
    case CreateOrUpdate(k, v) => storage forward CreateOrUpdate(k, v)
    case Read(k) => storage forward Read(k)
    case Delete(k) => storage forward Delete(k)
  }

}

object ProcessRequests {
  def props(storage: ActorRef) = Props(new ProcessRequests(storage))
}
