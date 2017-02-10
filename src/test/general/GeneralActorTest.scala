package general

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, Suite, WordSpecLike}

/**
  * Mixin actor test
  *
  */
class GeneralActorTest extends TestKit(ActorSystem("test"))
  with WordSpecLike
  with ImplicitSender
  with StopSystemAfterAll {
}

trait StopSystemAfterAll extends BeforeAndAfterAll {
  this: TestKit with Suite =>
  override protected def afterAll(): Unit = {
    super.afterAll()
    system.terminate()
  }
}

