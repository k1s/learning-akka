package model

import slick.lifted.{TableQuery, Tag}
import slick.model.Table

case class User(name: String)

//class Users(tag: Tag) extends Table[(Int, String)](tag, "users") {
//
//  def id = column[Int]("id", O.AutoInc)
//
//  def name = column[String]("name", O.PrimaryKey)
//
//  def password = column[String]("password")
//
//  def * = (id, name)
//
//}

object User {
//  val users = TableQuery[Users]
}
