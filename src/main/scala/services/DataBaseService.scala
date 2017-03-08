package services

import slick.basic.DatabaseConfig
import slick.jdbc.{JdbcBackend, JdbcProfile}

case class DataBaseService(configName: String) {
  val dbConfig = DatabaseConfig.forConfig[JdbcProfile](configName)
  val db: JdbcBackend#DatabaseDef = dbConfig.db
  db.createSession()
}