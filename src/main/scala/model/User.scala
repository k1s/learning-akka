package model

import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag

case class User(id: Int, name: String)

case class UserEntity(id: Option[Int] = None, name: String, password: String)

class UsersTable(tag: Tag) extends Table[UserEntity](tag, "users") {

  def id = column[Option[Int]]("id", O.AutoInc)

  def name = column[String]("name", O.PrimaryKey)

  def password = column[String]("password")

  def * = (id, name, password) <> (UserEntity.tupled, UserEntity.unapply)

}

object UsersTable {

  val users = TableQuery[UsersTable]

  val systemUser = User(1, "system")

}