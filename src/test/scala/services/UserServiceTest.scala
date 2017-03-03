package services

  import java.util.concurrent.TimeUnit

import akka.pattern.ask
import akka.testkit.TestActorRef
import akka.util.Timeout
import general.GeneralTest
import model.UserEntity
import services.UserService.GetUserByName

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

/**
  *
  */
class UserServiceTest extends GeneralTest {

  "UserService" must {

    "return User by name" in {

      val actorRef = TestActorRef(UserService.props(db))

      implicit val timeout = Timeout(5, TimeUnit.SECONDS)

      val future =
        (actorRef ? GetUserByName("admin")).asInstanceOf[Future[Future[Option[UserEntity]]]] flatMap identity

      val user = Await.result(future, Duration.Inf).get

      assert(UserEntity(Some(1), "admin") == user)

    }

  }

}
