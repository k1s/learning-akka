package app

import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.config.Config
import router.Router

import scala.concurrent.ExecutionContext
import services._
import services.log.LogService
import storage._

import scala.concurrent.duration.Duration

object StorageApp extends App with ConfigService {
  implicit val system = ActorSystem()
  implicit val executionContext = system.dispatcher
  implicit val log = Logging(system, getClass)
  implicit val materializer = ActorMaterializer()

  val database = new DataBaseService(jdbcUrl, dbUser, dbPassword, maxConnections)
  val logger = system.actorOf(LogService.props(database.db))
  val storage = system.actorOf(Storage.props(shardsNum, logger))
  val router = new Router(system, Timeout(timeoutSeconds, TimeUnit.SECONDS), storage)

  Http().bindAndHandle(router.routes, interface = httpHost, port = httpPort)
}
