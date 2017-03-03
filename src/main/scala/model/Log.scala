package model

import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag

case class Log(user: User, message: String)

case class LogEntity(id: Option[Int], text: String, userId: Option[Int])

class LogsTable(tag: Tag) extends Table[LogEntity](tag, "logs") {

  def id = column[Option[Int]]("id", O.AutoInc, O.PrimaryKey)

  def text = column[String]("text")

  def userId = column[Option[Int]]("user_id")

  def user = foreignKey("logs_user_fk", userId, UsersTable.users)(_.id)

  def * = (id, text, userId) <> (LogEntity.tupled, LogEntity.unapply)

}

object LogsTable {
  val logs = TableQuery[LogsTable]
}

