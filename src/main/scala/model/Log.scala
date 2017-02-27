package model

import slick.lifted.Tag
import slick.model.Table
//import slick.model.Table

case class Log(s: String)

class Logs(tag: Tag) extends Table[(Int, String, Int)](tag, "logs") {

  def id = column[Int]("id", O.AutoInc, O.PrimaryKey)

  def text = column[String]("text")

  def userId = column[Int]("user_id")

  def user = foreignKey("logs_user_fk", userId, User.users)(_.id)
}
