package general

import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.util.{ByteString, Timeout}
import org.scalatest.{Matchers, WordSpec}
import router.Router
import storage.Storage

import scala.concurrent.duration.FiniteDuration

/**
  *
  */
class RouterIntegrationTest extends WordSpec
  with Matchers
  with ScalatestRouteTest {

  val storage = system.actorOf(Storage.props(7))
  val router = new Router(system, Timeout.durationToTimeout(FiniteDuration(50, "millis")), storage)
  val path = s"/${router.restPath}"

  "StorageRouter" should {

    "response with error" in {
      Get(s"$path/1") ~> router.routes ~> check {
        status shouldEqual StatusCodes.InternalServerError
      }
    }

    "create" in {
      val json = ByteString(s"""{"key":1, "value":"123"}""")

      val request = HttpRequest(
        HttpMethods.POST,
        uri = path,
        entity = HttpEntity(MediaTypes.`application/json`, json))

      request ~> router.routes ~> check {
        status shouldEqual StatusCodes.OK
      }
    }

    "create and read" in {
      val json = ByteString(s"""{"key":1, "value":"123"}""")

      val request = HttpRequest(
        HttpMethods.POST,
        uri = path,
        entity = HttpEntity(MediaTypes.`application/json`, json))

      request ~> router.routes ~> check {
        status shouldEqual StatusCodes.OK
      }

      Get(s"$path/1") ~> router.routes ~> check {
        status shouldEqual StatusCodes.OK
        responseAs[String] shouldEqual "123"
      }
    }

    "create and delete" in {
      val json = ByteString(s"""{"key":1, "value":"123"}""")

      val request = HttpRequest(
        HttpMethods.POST,
        uri = path,
        entity = HttpEntity(MediaTypes.`application/json`, json))

      request ~> router.routes ~> check {
        status shouldEqual StatusCodes.OK
      }

      Delete(s"$path/1") ~> router.routes ~> check {
        status shouldEqual StatusCodes.OK
      }

      Delete(s"$path/1") ~> router.routes ~> check {
        status shouldEqual StatusCodes.InternalServerError
      }
    }

    "create, read, update and delete" in {
      val json = ByteString(s"""{"key":1, "value":"123"}""")

      val request = HttpRequest(
        HttpMethods.POST,
        uri = path,
        entity = HttpEntity(MediaTypes.`application/json`, json))

      request ~> router.routes ~> check {
        status shouldEqual StatusCodes.OK
      }

      val json1 = ByteString(s"""{"key":1, "value":"abc"}""")

      val request1 = HttpRequest(
        HttpMethods.POST,
        uri = path,
        entity = HttpEntity(MediaTypes.`application/json`, json1))

      request1 ~> router.routes ~> check {
        status shouldEqual StatusCodes.OK
      }

      Get(s"$path/1") ~> router.routes ~> check {
        status shouldEqual StatusCodes.OK
        responseAs[String] shouldEqual "abc"
      }

      Delete(s"$path/1") ~> router.routes ~> check {
        status shouldEqual StatusCodes.OK
      }

    }

  }

}
