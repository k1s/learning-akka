package services

import java.util.concurrent.TimeUnit

import akka.util.Timeout
import general.GeneralTest
import model.UserEntity

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

/**
  *
  */
class UserServiceTest extends GeneralTest {

  "UserService" must {

    "return User by name" in {

      val userService = UserService(testDataBaseService)

      implicit val timeout = Timeout(5, TimeUnit.SECONDS)

      val future: Future[Option[UserEntity]] = userService.getUserEntityByName("system")

      val user = Await.result(future, Duration.Inf).get

      assert("system" == user.name)
      assert(1 == user.id.get)

    }

  }

}
