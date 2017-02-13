package storage

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorRef, Props}

import scala.collection.mutable
import messages._

/**
  *
  */
class Storage(shardsSize: Int, shard: Shard) extends Actor {

  var currentId = 0

  var shards = Vector.fill(shardsSize)(context.actorOf(Shard.props))

  def bits = (Math.log(shardsSize) / Math.log(2)).toInt

  /**
    * When there are 2&#94;n shards, the first n bits of the hash code of the key are used
    * to decide which shard a key-value pair should go to.
    */
  def shardNumber(k: Int): Int = k.hashCode().toBinaryString.takeRight(bits).toInt

  def receive = ???

}

object Storage {
  def props(shardsSize: Int, shard: Shard) = Props(new Storage(shardsSize, shard))
}
