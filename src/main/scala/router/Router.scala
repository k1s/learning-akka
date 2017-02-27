package router

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.util.Timeout
import spray.json.DefaultJsonProtocol._
import model.User
import services.storage.Storage._

/**
  * Storage API
  *
  */
class Router(system: ActorSystem, timeout: Timeout, storage: ActorRef) {

  import model.Messages._

  case class KeyValue(key: Int, value: String)

  implicit val kvFormat = jsonFormat2(KeyValue)

  implicit def executionContext = system.dispatcher

  implicit def requestTimeout = timeout

  val restPath = "storage"

  val routes = createValue ~ updateValue ~ readValue ~ deleteValue
//  todo k1s add user
  val user = new User

  val standardResponse: (Any) => server.Route = {
    case Complete => complete(StatusCodes.OK)
    case Error => complete(StatusCodes.InternalServerError)
  }

  def createValue =
    post {
      pathPrefix(restPath / Segment) { value =>
          onSuccess(storage.ask(Create(user, value))) {
            case Key(k) => complete(k.toString)
            case Error => complete(StatusCodes.InternalServerError)
        }
      }
    }

  def updateValue =
    post {
      path(restPath) {
        entity(as[KeyValue]) { kv =>
          onSuccess(storage.ask(Update(user, kv.key, kv.value))) {
            standardResponse
          }
        }
      }
    }

  def readValue =
    get {
      pathPrefix(restPath / IntNumber) { id =>
        onSuccess(storage.ask(Read(user, id))) {
          case Value(v) => complete(v)
          case Error => complete(StatusCodes.InternalServerError)
        }
      }
    }

  def deleteValue =
    delete {
      pathPrefix(restPath / IntNumber) { id =>
        onSuccess(storage.ask(Delete(user, id))) {
          standardResponse
        }
      }
    }

}
