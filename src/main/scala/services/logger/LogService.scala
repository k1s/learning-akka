package services.logger

import akka.actor.{Actor, Props}
import model.User
import services.DataBaseService
import services.logger.LogService.Log

/**
  * Interface for logging. Services that use it do not want know how it will process log messages
  *
  */
class LogService(dataBaseService: DataBaseService) extends Actor {

  def receive = {
    case log@ Log(user, message) =>
      val writer = context.actorOf(LogWriter.props(dataBaseService))
      writer ! log
  }

}

object LogService {

  case class Log(user: User, message: String)

  def props(dataBaseService: DataBaseService) = Props(new LogService(dataBaseService))

}
