package storage

import akka.actor.{Actor, Props}
import messages._

/**
  * Storage, shards size must be > 0
  *
  */
class Storage(shardsSize: Int) extends Actor {

  import Storage._

  require(shardsSize > 0)

  var currentKey = 0

  val shards = Vector.fill(shardsSize)(context.actorOf(Shard.props))

  def forwardToShard(message: IncomeMessage) = {
    val num = shardNumber(message.key, shardsSize)
    shards(num) forward message
  }

  def receive = {

    case Create(v) =>
      val num = shardNumber(currentKey, shardsSize)
      shards(num) ! Update(currentKey, v)
      sender() ! Key(currentKey)
      currentKey += 1

    case message: IncomeMessage =>
      forwardToShard(message)

  }

}

object Storage {

  /*
   * Props of Storage, shard size must be > 0
   */
  def props(shardsSize: Int) = Props(new Storage(shardsSize))

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
