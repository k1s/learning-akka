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
class Router(system: ActorSystem, timeout: Timeout, processRequests: ActorRef) {

  import messages._

  implicit def executionContext = system.dispatcher

  implicit def requestTimeout = timeout

  implicit val createFormat = jsonFormat2(CreateOrUpdate)
  implicit val readFormat = jsonFormat1(Read)
  implicit val deleteFormat = jsonFormat1(Delete)

  val restPath = "storage"

  val routes = createOrUpdateValue ~ readValue ~ deleteValue

  val standardResponse: (Any) => server.Route = {
    case Complete => complete(StatusCodes.OK)
    case Error => complete(StatusCodes.InternalServerError)
  }

  def createOrUpdateValue =
    post {
      path(restPath) {
        entity(as[CreateOrUpdate]) { create =>
          onSuccess(processRequests.ask(create)) {
            standardResponse
          }
        }
      }
    }

  def readValue =
    get {
      pathPrefix(restPath / IntNumber) { id =>
        onSuccess(processRequests.ask(Read(id))) {
          case Value(v) => complete(v)
          case Error => complete(StatusCodes.InternalServerError)
        }
      }
    }

  def deleteValue =
    delete {
      pathPrefix(restPath / IntNumber) { id =>
        onSuccess(processRequests.ask(Delete(id))) {
          standardResponse
        }
      }
    }

}
