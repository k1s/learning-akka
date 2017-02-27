package services

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import slick.jdbc.JdbcBackend.Database

class DataBaseService(jdbcUrl: String, dbUser: String, dbPassword: String, maxConnections: Int) {
  private val hikariConfig = new HikariConfig()
  hikariConfig.setJdbcUrl(jdbcUrl)
  hikariConfig.setUsername(dbUser)
  hikariConfig.setPassword(dbPassword)
  hikariConfig.setDriverClassName("slick.driver.PostgresDriver$")

  private val dataSource = new HikariDataSource(hikariConfig)

//  val driver = slick.jdbc.PostgresProfile
  val db: Database  = Database.forDataSource(dataSource, maxConnections = Some(maxConnections))
  db.createSession()
}