package services.logger

import akka.actor.{Actor, PoisonPill, Props}
import model.LogsTable.logs
import model.{LogsTable, UsersTable}
import services.DataBaseService
import services.logger.LogService.Log

import scala.util.{Failure, Success}

/**
  *
  */
class LogWriter(dataBaseService: DataBaseService) extends Actor {

  import dataBaseService.db
  import dataBaseService.dbConfig.profile.api._

  import scala.concurrent.ExecutionContext.Implicits.global

  def receive = {
//    todo k1s test
    case Log(user, message) => insert(user.id, message) onComplete {

        case Success(_) =>
          self ! PoisonPill

        case Failure(f) =>
          context.parent ! Log(UsersTable.systemUser, f.getMessage)
          self ! PoisonPill

      }
  }

  def insert(userId: Int, message: String) = db.run(logs += LogsTable.toEntity(userId, message))

}

object LogWriter {
  def props(dataBaseService: DataBaseService) = Props(new LogWriter(dataBaseService))
}
