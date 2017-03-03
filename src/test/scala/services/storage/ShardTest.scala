package services.storage

import general.GeneralTest

import model.Messages._
import Shard._

/**
  *
  */
class ShardTest extends GeneralTest {

    "Shard" must {

      "ignore wrong model.messages" in {
        val shard = system.actorOf(Shard.props)

        shard ! Error

        expectNoMsg()
      }

      "create value" in {
        val shard = system.actorOf(Shard.props)

        shard ! UpdateShard(1, "123")

        expectMsg(Complete)
      }

      "create and read value" in {
        val shard = system.actorOf(Shard.props)

        shard ! UpdateShard(1, "123")

        expectMsg(Complete)

        shard ! ReadShard(1)

        expectMsg(Value("123"))
      }

      "create and delete value" in {
        val shard = system.actorOf(Shard.props)

        shard ! UpdateShard(1, "123")

        expectMsg(Complete)

        shard ! DeleteShard(1)

        expectMsg(Complete)

        shard ! DeleteShard(1)

        expectMsg(Error)
      }

      "create and update value" in {
        val shard = system.actorOf(Shard.props)

        shard ! UpdateShard(1, "123")

        expectMsg(Complete)

        shard ! UpdateShard(1, "abc")

        expectMsg(Complete)

        shard ! ReadShard(1)

        expectMsg(Value("abc"))
      }

    }

}
