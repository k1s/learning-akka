package services

import akka.actor.{Actor, Props}
import model.Log
import slick.jdbc.JdbcBackend

class LogService(db: JdbcBackend#DatabaseDef) extends Actor {

  def receive = {
    case Log(u, message) => ???
  }

}

object LogService {
  def props(db: JdbcBackend#DatabaseDef) = Props(new LogService(db))
}
