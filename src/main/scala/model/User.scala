package model

import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag

case class User(name: String)

case class UserEntity(id: Option[Int] = None, name: String)

class UsersTable(tag: Tag) extends Table[UserEntity](tag, "users") {

  def id = column[Option[Int]]("id", O.AutoInc)

  def name = column[String]("name", O.PrimaryKey)

  def * = (id, name) <> (UserEntity.tupled, UserEntity.unapply)

}

object UsersTable {
  val users = TableQuery[UsersTable]
}