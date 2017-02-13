package router

import akka.actor.{Actor, Props}
import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.util.{ByteString, Timeout}
import org.scalatest.{Matchers, WordSpec}
import messages._
import scala.concurrent.duration._

/**
  *
  */
class RouterTest extends WordSpec
  with Matchers
  with ScalatestRouteTest {

  val num = 42

  class testProcessRequest extends Actor {
    def receive = {
      case Create(v) => sender() ! Key(num)
      case Update(k, v) => sender() ! Complete
      case Read(k) => sender() ! Error
      case messages.Delete(k) => sender() ! Complete
    }
  }

  val router = new Router(system, Timeout(1 seconds), system.actorOf(Props(new testProcessRequest)))
  val path = s"/${router.restPath}"

  "StorageRouter" should {

    "response with error for get" in {
      Get(s"$path/1") ~> router.routes ~> check {
        status shouldEqual StatusCodes.InternalServerError
      }
    }

    "response with complete for delete" in {
      Delete(s"$path/1") ~> router.routes ~> check {
        status shouldEqual StatusCodes.OK
      }
    }

    "response with num for new post" in {
      Post(s"$path/aaa") ~> router.routes ~> check {
        status shouldEqual StatusCodes.OK
        responseAs[String] shouldEqual num.toString
      }
    }

    "handle key-value in json" in {
      val json = ByteString(s"""{"key":1, "value":"2"}""")

      val request = HttpRequest(
        HttpMethods.POST,
        uri = path,
        entity = HttpEntity(MediaTypes.`application/json`, json))

      request ~> router.routes ~> check {
        status shouldEqual StatusCodes.OK
      }
    }

  }

}
