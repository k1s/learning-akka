package services.storage

import akka.actor.{Actor, Props}
import Shard._
import model.Messages._
/**
  * Shard is element of services.storage
  */
class Shard extends Actor {

  var map = Map[Int, String]()

  def receive = {

    case UpdateShard(k, v) =>
      map = map.updated(k, v)
      sender() ! Complete

    case ReadShard(k) =>
      map.get(k) match {
        case Some(v) => sender() ! Value(v)
        case None => sender() ! Error
      }

    case DeleteShard(k) =>
      if (map.contains(k)) {
        map = map - k
        sender() ! Complete
      } else {
        sender() ! Error
      }

  }

}

object Shard {

  sealed trait ShardMessage {val key: Int}

  case class UpdateShard(key: Int, value: String) extends ShardMessage
  case class ReadShard(key: Int) extends ShardMessage
  case class DeleteShard(key: Int) extends ShardMessage

  def props = Props(new Shard)
}
