package services.storage

import general.GeneralTest
import services.storage.Storage._
import model.Messages._
import model.User


/**
  *
  */
class StorageTest extends GeneralTest {

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

    val user = User(42, "user")


    "response with error" in {
      val storage = system.actorOf(Storage.props(7, emptyLogger))

      storage ! Read(user, 100500)

      expectMsg(Error)
    }

    "ignore wrong model.messages" in {
      val storage = system.actorOf(Storage.props(7, emptyLogger))

      storage ! Error

      expectNoMsg()
    }

    "create value" in {
      val storage = system.actorOf(Storage.props(7, emptyLogger))

      storage ! Create(user, "123")

      expectMsg(Key(0))
    }

    "create values" in {
      val storage = system.actorOf(Storage.props(7, emptyLogger))

      storage ! Create(user, "123")

      expectMsg(Key(0))

      storage ! Create(user, "abc")

      expectMsg(Key(1))
    }

    "create and read value" in {
      val storage = system.actorOf(Storage.props(7, emptyLogger))

      storage ! Create(user, "123")

      expectMsg(Key(0))

      storage ! Read(user, 0)

      expectMsg(Value("123"))
    }

    "create and delete value" in {
      val storage = system.actorOf(Storage.props(7, emptyLogger))

      storage ! Create(user, "123")

      expectMsg(Key(0))

      storage ! Delete(user, 0)

      expectMsg(Complete)

      storage ! Delete(user, 1)

      expectMsg(Error)
    }

    "create and update value" in {
      val storage = system.actorOf(Storage.props(7, emptyLogger))

      storage ! Create(user, "123")

      expectMsg(Key(0))

      storage ! Update(user, 1, "abc")

      expectMsg(Complete)

      storage ! Read(user, 1)

      expectMsg(Value("abc"))
    }

  }
}
