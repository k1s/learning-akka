package storage

import akka.actor.{Actor, Props}
import scala.collection.mutable

import messages._

/**
  *
  */
class Storage extends Actor {

  val map = new mutable.HashMap[Int, String]()

  def receive = {
    case CreateOrUpdate(k, v) =>
      map.put(k, v)
      sender() ! Complete
    case Read(k) => map.get(k) match {
        case Some(v) => sender() ! Value(v)
        case None => sender() ! Error
      }
    case messages.Delete(k) => map.remove(k) match {
      case Some(v) => sender() ! Complete
      case None => sender() ! Error
    }
  }

}

object Storage {
  def props = Props(new Storage)
}
