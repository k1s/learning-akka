package general

import akka.actor.{Actor, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, Suite, WordSpecLike}
import services.log.LogService

/**
  * Mixin actor test
  *
  */
class ActorTest extends TestKit(ActorSystem("test"))
  with WordSpecLike
  with ImplicitSender
  with StopSystemAfterAll {
  val emptyLogger = system.actorOf(Props(new EmptyLogger))
}

class EmptyLogger extends Actor {
  def receive = ???
}

trait StopSystemAfterAll extends BeforeAndAfterAll {
  this: TestKit with Suite =>
  override protected def afterAll(): Unit = {
    super.afterAll()
    system.terminate()
  }
}

