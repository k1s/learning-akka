package services.log

import akka.actor.{Actor, Props}
import slick.jdbc.JdbcBackend.Database

class LogService(db: Database) extends Actor {

  def receive = ???
//  create new LogWriter per request
}

object LogService {
  def props(db: Database) = Props(new LogService(db))
}
