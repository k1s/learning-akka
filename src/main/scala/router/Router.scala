package router

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.util.Timeout
import spray.json.DefaultJsonProtocol._

/**
  * Storage API
  *
  */
class Router(system: ActorSystem, timeout: Timeout, storage: ActorRef) {

  import messages._

  implicit def executionContext = system.dispatcher

  implicit def requestTimeout = timeout

  implicit val createFormat = jsonFormat1(Create)
  implicit val readFormat = jsonFormat1(Read)
  implicit val updateFormat = jsonFormat2(Update)
  implicit val deleteFormat = jsonFormat1(Delete)

  val restPath = "storage"

  val routes = createValue ~ updateValue ~ readValue ~ deleteValue

  val standardResponse: (Any) => server.Route = {
    case Complete => complete(StatusCodes.OK)
    case Error => complete(StatusCodes.InternalServerError)
  }

  def createValue =
    post {
      pathPrefix(restPath / Segment) { value =>
          onSuccess(storage.ask(Create(value))) {
            case Key(k) => complete(k.toString)
            case Error => complete(StatusCodes.InternalServerError)
        }
      }
    }

  def updateValue =
    post {
      path(restPath) {
        entity(as[Update]) { update =>
          onSuccess(storage.ask(update)) {
            standardResponse
          }
        }
      }
    }

  def readValue =
    get {
      pathPrefix(restPath / IntNumber) { id =>
        onSuccess(storage.ask(Read(id))) {
          case Value(v) => complete(v)
          case Error => complete(StatusCodes.InternalServerError)
        }
      }
    }

  def deleteValue =
    delete {
      pathPrefix(restPath / IntNumber) { id =>
        onSuccess(storage.ask(Delete(id))) {
          standardResponse
        }
      }
    }

}
