package storage

import general.GeneralActorTest

import scala.concurrent.duration.FiniteDuration
import messages._

/**
  *
  */
class ShardTest extends GeneralActorTest {

    "Shard" must {

      "ignore wrong messages" in {
        val shard = system.actorOf(Shard.props)

        shard ! Error

        expectNoMsg()
      }

      "create value" in {
        val shard = system.actorOf(Shard.props)

        shard ! Put(1, "123")

        expectMsg(FiniteDuration(50, "millis"), Complete)
      }

      "create and read value" in {
        val shard = system.actorOf(Shard.props)

        shard ! Put(1, "123")

        expectMsg(FiniteDuration(50, "millis"), Complete)

        shard ! Read(1)

        expectMsg(FiniteDuration(50, "millis"), Value("123"))
      }

      "create and delete value" in {
        val shard = system.actorOf(Shard.props)

        shard ! Put(1, "123")

        expectMsg(FiniteDuration(50, "millis"), Complete)

        shard ! Delete(1)

        expectMsg(FiniteDuration(50, "millis"), Complete)

        shard ! Delete(1)

        expectMsg(FiniteDuration(50, "millis"), Error)
      }

      "create and update value" in {
        val shard = system.actorOf(Shard.props)

        shard ! Put(1, "123")

        expectMsg(FiniteDuration(50, "millis"), Complete)

        shard ! Put(1, "abc")

        expectMsg(FiniteDuration(50, "millis"), Complete)

        shard ! Read(1)

        expectMsg(FiniteDuration(50, "millis"), Value("abc"))
      }

    }

}
