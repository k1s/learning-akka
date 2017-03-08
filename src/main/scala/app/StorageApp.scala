package app

import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.util.Timeout
import router.Router
import services.storage._
import services.{ConfigService, DataBaseService, UserService}
import services.logger.LogService

object StorageApp extends App with ConfigService {
  implicit val system = ActorSystem()
  implicit val executionContext = system.dispatcher
  implicit val log = Logging(system, getClass)
  implicit val materializer = ActorMaterializer()

  val database = DataBaseService("postgres")
  val logger = system.actorOf(LogService.props(database))
  val storage = system.actorOf(Storage.props(shardsNum, logger))
  val userService = UserService(database)
  val router = new Router(system, Timeout(timeoutSeconds, TimeUnit.SECONDS), storage, userService)

  Http().bindAndHandle(router.routes, interface = httpHost, port = httpPort)
}
