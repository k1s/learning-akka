package storage

import general.ActorTest

import scala.concurrent.duration.FiniteDuration
import messages._

/**
  *
  */
class ShardTest extends ActorTest {

    "Shard" must {

      "ignore wrong messages" in {
        val shard = system.actorOf(Shard.props)

        shard ! Error

        expectNoMsg()
      }

      "create value" in {
        val shard = system.actorOf(Shard.props)

        shard ! Update(1, "123")

        expectMsg(Complete)
      }

      "create and read value" in {
        val shard = system.actorOf(Shard.props)

        shard ! Update(1, "123")

        expectMsg(Complete)

        shard ! Read(1)

        expectMsg(Value("123"))
      }

      "create and delete value" in {
        val shard = system.actorOf(Shard.props)

        shard ! Update(1, "123")

        expectMsg(Complete)

        shard ! Delete(1)

        expectMsg(Complete)

        shard ! Delete(1)

        expectMsg(Error)
      }

      "create and update value" in {
        val shard = system.actorOf(Shard.props)

        shard ! Update(1, "123")

        expectMsg(Complete)

        shard ! Update(1, "abc")

        expectMsg(Complete)

        shard ! Read(1)

        expectMsg(Value("abc"))
      }

    }

}
