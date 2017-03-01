package router

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.Credentials
import akka.http.scaladsl.server.{AuthenticationFailedRejection, RejectionHandler}
import akka.pattern.ask
import akka.util.Timeout
import model.User
import services.storage.Storage._
import spray.json.DefaultJsonProtocol._

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

  val standardResponse: (Any) => server.Route = {
    case Complete => complete(StatusCodes.OK)
    case Error => complete(StatusCodes.InternalServerError)
  }

  def userAuthenticator(credentials: Credentials): Option[String] = credentials match {
    case Credentials.Provided(userName) => Some(userName)
    case _ => None
  }

  val rejectionHandler = RejectionHandler.newBuilder()
    .handleNotFound { complete((StatusCodes.NotFound, "NOT FOUND")) }
    .handle { case AuthenticationFailedRejection(msg, _) =>
      complete(StatusCodes.Unauthorized -> "The resource requires authentication")}
    .result()

  val routes = {
    handleRejections(rejectionHandler) {
      authenticateBasic(realm = "very secure", userAuthenticator) { userName =>
        post {
          pathPrefix(restPath / Segment) { value =>
            onSuccess(storage.ask(Create(User(userName), value))) {
              case Key(k) => complete(k.toString)
              case Error => complete(StatusCodes.InternalServerError)
            }
          }
        } ~ post {
          path(restPath) {
            entity(as[KeyValue]) { kv =>
              onSuccess(storage.ask(Update(User(userName), kv.key, kv.value))) {
                standardResponse
              }
            }
          }
        } ~ get {
          pathPrefix(restPath / IntNumber) { id =>
            onSuccess(storage.ask(Read(User(userName), id))) {
              case Value(v) => complete(v)
              case Error => complete(StatusCodes.InternalServerError)
            }
          }
        } ~ delete {
          pathPrefix(restPath / IntNumber) { id =>
            onSuccess(storage.ask(Delete(User(userName), id))) {
              standardResponse
            }
          }
        }
      }
    }
  }


}
