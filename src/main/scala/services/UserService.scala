package services

import akka.actor.{Actor, Props}
import model.UserEntity
import model.UsersTable._
import services.UserService.GetUserByName
import slick.jdbc.JdbcBackend
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future

/**
  *
  */
class UserService(db: JdbcBackend#DatabaseDef) extends Actor {

  def receive = {
    case GetUserByName(name: String) => sender() ! getUserByName(name)
  }

  def getUserByName(name: String): Future[Option[UserEntity]] =
    db.run(users.filter(_.name === name).result.headOption)

}

object UserService {
  case class GetUserByName(name: String)
  def props(db: JdbcBackend#DatabaseDef) = Props(new UserService(db))
}
