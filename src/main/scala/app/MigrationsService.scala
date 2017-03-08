package app

import org.flywaydb.core.Flyway

/**
  *
  */
case class MigrationsService(url: String, user: String, password: String) {

  val flyway = new Flyway()

  flyway.setDataSource(url, user, password)

  def migrateDatabaseSchema(): Unit = flyway.migrate()

  def dropDatabase(): Unit = flyway.clean()

}
