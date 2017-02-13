package storage

import akka.actor.{Actor, Props}
import messages._

/**
  *
  */
class Shard extends Actor {

  var map = Map[Int, String]()

  def receive = {
    case Put(k, v) =>
      map = map.updated(k, v)
      sender() ! Complete
    case Read(k) =>
      map.get(k) match {
        case Some(v) => sender() ! Value(v)
        case None => sender() ! Error
      }
    case messages.Delete(k) =>
      if (map.contains(k)) {
        map = map - k
        sender() ! Complete
      } else {
        sender() ! Error
      }
  }

}

object Shard {
  def props = Props(new Shard)
}
