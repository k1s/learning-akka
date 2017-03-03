package app

import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.util.Timeout
import router.Router
import services.storage._
import services.{DataBaseService, LogService, _}

object StorageApp extends App with ConfigService {
  implicit val system = ActorSystem()
  implicit val executionContext = system.dispatcher
  implicit val log = Logging(system, getClass)
  implicit val materializer = ActorMaterializer()

  val database = new DataBaseService
  val logger = system.actorOf(LogService.props(database.db))
  val storage = system.actorOf(Storage.props(shardsNum, logger))
  val router = new Router(system, Timeout(timeoutSeconds, TimeUnit.SECONDS), storage)

  Http().bindAndHandle(router.routes, interface = httpHost, port = httpPort)
}
