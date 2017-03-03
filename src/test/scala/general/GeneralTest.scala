package general

import akka.actor.{Actor, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import model.UserEntity
import org.scalatest.{BeforeAndAfterAll, Suite, WordSpecLike}
import slick.basic.DatabaseConfig
import slick.jdbc.{JdbcBackend, JdbcProfile}

/**
  * Test mixin
  *
  */
class GeneralTest extends TestKit(ActorSystem("test"))
  with WordSpecLike
  with ImplicitSender
  with StopSystemAfterAll {
  val emptyLogger = system.actorOf(Props(new EmptyLogger))
  val db = TestDataBaseService.db
}

object TestDataBaseService {
  val dbConfig = DatabaseConfig.forConfig[JdbcProfile]("testPostgres")
  val db: JdbcBackend#DatabaseDef = dbConfig.db
  db.createSession()
  import dbConfig.profile.api._
  import model.LogsTable._
  import model.UsersTable._
//  val ddl = DBIO.seq(
//    users.schema.create,
//    logs.schema.create,
//    users += UserEntity(Some(1), "admin")
//  )
//  db.run(ddl)
//  todo k1s seq dbio are not working?
  db.run(users.schema.create)
  db.run(logs.schema.create)
  db.run(users += UserEntity(Some(1), "admin"))
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

