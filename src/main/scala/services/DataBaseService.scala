package services

import slick.basic.DatabaseConfig
import slick.jdbc.{JdbcBackend, JdbcProfile}

class DataBaseService {
  val dbConfig = DatabaseConfig.forConfig[JdbcProfile]("postgres")
  val db: JdbcBackend#DatabaseDef = dbConfig.db
  db.createSession()
}