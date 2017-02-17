package storage

import general.ActorTest
import storage.Storage._

/**
  *
  */
class StorageTest extends ActorTest {

  import messages._

  "Storage" must {

    "Create right shard number for size of 1" in {

      assert(0 == shardNumber(0, 1))
      assert(0 == shardNumber(1, 1))
      assert(0 == shardNumber(100, 1))
      assert(0 == shardNumber(1000, 1))

    }

    "Create right shard number for even size" in {

      assert(0 == shardNumber(0, 2))
      assert(1 == shardNumber(1, 2))
      assert(0 == shardNumber(100, 2))
      assert(1 == shardNumber(101, 2))
      assert(0 == shardNumber(1000, 2))
      assert(1 == shardNumber(1001, 2))

    }

    "Create right shard number for not even size" in {

      assert(2 == shardNumber(0, 3))
      assert(1 == shardNumber(1, 3))
      assert(0 == shardNumber(2, 3))

      assert(2 == shardNumber(3, 3))
      assert(0 == shardNumber(4, 3))
      assert(1 == shardNumber(5, 3))

      assert(0 == shardNumber(100, 3))
      assert(1 == shardNumber(101, 3))
      assert(2 == shardNumber(102, 3))

      assert(1 == shardNumber(103, 3))
      assert(0 == shardNumber(104, 3))
      assert(2 == shardNumber(105, 3))

    }


    "response with error" in {
      val storage = system.actorOf(Storage.props(7))

      storage ! Read(100500)

      expectMsg(Error)
    }

    "ignore wrong messages" in {
      val storage = system.actorOf(Storage.props(7))

      storage ! Error

      expectNoMsg()
    }

    "create value" in {
      val storage = system.actorOf(Storage.props(7))

      storage ! Create("123")

      expectMsg(Key(0))
    }

    "create values" in {
      val storage = system.actorOf(Storage.props(7))

      storage ! Create("123")

      expectMsg(Key(0))

      storage ! Create("abc")

      expectMsg(Key(1))
    }

    "create and read value" in {
      val storage = system.actorOf(Storage.props(7))

      storage ! Create("123")

      expectMsg(Key(0))

      storage ! Read(0)

      expectMsg(Value("123"))
    }

    "create and delete value" in {
      val storage = system.actorOf(Storage.props(7))

      storage ! Create("123")

      expectMsg(Key(0))

      storage ! Delete(0)

      expectMsg(Complete)

      storage ! Delete(1)

      expectMsg(Error)
    }

    "create and update value" in {
      val storage = system.actorOf(Storage.props(7))

      storage ! Create("123")

      expectMsg(Key(0))

      storage ! Update(1, "abc")

      expectMsg(Complete)

      storage ! Read(1)

      expectMsg(Value("abc"))
    }

  }
}
