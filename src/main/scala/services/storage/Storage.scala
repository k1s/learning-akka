package services.storage

import akka.actor.{Actor, ActorRef, Props}
import model.{Log, User}
import model.Messages._
import services.storage.Shard.{ShardMessage, _}
import services.storage.Storage._

/**
  * Storage, shards size must be > 0
  *
  */
class Storage(shardsSize: Int, logService: ActorRef) extends Actor {

  require(shardsSize > 0)

  var currentKey = 0

  val shards = Vector.fill(shardsSize)(context.actorOf(Shard.props))

  def forwardToShard(message: ShardMessage) = {
    val num = shardNumber(message.key, shardsSize)
    shards(num) forward message
  }

  def receive = {

    case Create(u, v) =>
      val num = shardNumber(currentKey, shardsSize)
      shards(num) ! UpdateShard(currentKey, v)
      sender() ! Key(currentKey)
      logService ! Log(u, s"Create $v with id $currentKey")
      currentKey += 1

    case Read(u, k) =>
      logService ! Log(u, s"Read value with key $k")
      forwardToShard(ReadShard(k))

    case Update(u, k, v) =>
      logService ! Log(u, s"Update key $k with value $v")
      forwardToShard(UpdateShard(k, v))

    case Delete(u, k) =>
      logService ! Log(u, s"Delete key $k")
      forwardToShard(DeleteShard(k))

  }

}

object Storage {

  case class Create(user: User, value: String)
  case class Read(user: User, key: Int)
  case class Update(user: User, key: Int, value: String)
  case class Delete(user: User, key: Int)

  /*
   * Props of Storage, shard size must be > 0
   */
  def props(shardsSize: Int, logService: ActorRef) = Props(new Storage(shardsSize, logService))

  /**
    * When there are 2&#94;n shards, the first n bits of the hash code of the key are used
    * to decide which shard a key-value pair should go to.
    */
  def shardNumber(key: Int, shardsSize: Int): Int = {
    if (shardsSize < 2)
      0
    else if (shardsSize % 2 != 0 && key % shardsSize == 0)
      shardsSize - 1
    else {
      val bits = (Math.log(shardsSize) / Math.log(2)).toInt
      Integer.parseInt(key.toBinaryString.takeRight(bits), 2)
    }
  }

}
