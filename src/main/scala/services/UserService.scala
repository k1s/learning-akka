package services

import model.UserEntity
import model.UsersTable._

import scala.concurrent.Future

/**
  *
  */
case class UserService(dataBaseService: DataBaseService) {

  import dataBaseService.db
  import dataBaseService.dbConfig.profile.api._

  def getUserEntityByName(name: String): Future[Option[UserEntity]] = db.run(users.filter(_.name === name).result.headOption)

}
