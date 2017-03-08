package general

import akka.actor.{Actor, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import app.MigrationsService
import model.{UserEntity, UsersTable}
import org.scalatest.{BeforeAndAfterAll, Matchers, Suite, WordSpecLike}
import services.DataBaseService

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Test mixin
  *
  */
case class GeneralTest() extends TestKit(ActorSystem("test"))
  with WordSpecLike
  with Matchers
  with ImplicitSender
  with StopSystemAfterAll {
  val emptyLogger = system.actorOf(Props(new EmptyLogger))
  val testDataBaseService = DataBaseService("testPostgres")

  def populateDatabase() = {

    val migrationService = MigrationsService(
      url = "jdbc:postgresql://localhost/test",
      user = "postgres",
      password = "postgres"
    )

    migrationService.dropDatabase()
    migrationService.migrateDatabaseSchema()

    import testDataBaseService.db
    import testDataBaseService.dbConfig.profile.api._

    Await.result(db.run(UsersTable.users += UserEntity(None, "system", "password")), Duration.Inf)

  }

  populateDatabase()

}

class EmptyLogger extends Actor {
  def receive = {
    case _ =>
  }
}

trait StopSystemAfterAll extends BeforeAndAfterAll {
  this: TestKit with Suite =>
  override protected def afterAll(): Unit = {
    super.afterAll()
    system.terminate()
  }
}

