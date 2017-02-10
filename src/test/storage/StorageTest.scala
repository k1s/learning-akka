package storage

import general.GeneralActorTest
import messages._

import scala.concurrent.duration.FiniteDuration

/**
  *
  */
class StorageTest extends GeneralActorTest {

  "Storage" must {

    "ignore wrong messages" in {
      val storage = system.actorOf(Storage.props)

      storage ! Error

      expectNoMsg()
    }

    "create value" in {
      val storage = system.actorOf(Storage.props)

      storage ! CreateOrUpdate(1, "123")

      expectMsg(FiniteDuration(50, "millis"), Complete)
    }

    "create and read value" in {
      val storage = system.actorOf(Storage.props)

      storage ! CreateOrUpdate(1, "123")

      expectMsg(FiniteDuration(50, "millis"), Complete)

      storage ! Read(1)

      expectMsg(FiniteDuration(50, "millis"), Value("123"))
    }

    "create and delete value" in {
      val storage = system.actorOf(Storage.props)

      storage ! CreateOrUpdate(1, "123")

      expectMsg(FiniteDuration(50, "millis"), Complete)

      storage ! Delete(1)

      expectMsg(FiniteDuration(50, "millis"), Complete)

      storage ! Delete(1)

      expectMsg(FiniteDuration(50, "millis"), Error)
    }

    "create and update value" in {
      val storage = system.actorOf(Storage.props)

      storage ! CreateOrUpdate(1, "123")

      expectMsg(FiniteDuration(50, "millis"), Complete)

      storage ! CreateOrUpdate(1, "abc")

      expectMsg(FiniteDuration(50, "millis"), Complete)

      storage ! Read(1)

      expectMsg(FiniteDuration(50, "millis"), Value("abc"))
    }

  }

}
