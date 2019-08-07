package com.intercax.syndeia.controllers

import com.intercax.syndeia.domain.DomainEntity
import com.intercax.syndeia.setup.domain.UserSetup
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, Matchers}

class BaseControllerSpec extends AsyncWordSpec with Matchers with BeforeAndAfterAll {

  var token: String = _

  override def beforeAll(): Unit = {
    token = UserSetup.getToken

    //    StoreState.emptyStore onComplete {
    //      case Success(storeState) =>
    //        logger.info(s"deleted all data")
    //
    //      case Failure(ex) => logger.error(s"unexpected error in cleanup: ${ex.getMessage}", ex)
    //    }
  }

  protected def getKey[T <: DomainEntity](eitherT: Either[String, T]): String = {
    if (eitherT.isRight) {
      eitherT.right.get.getKey
    } else {
      ""
    }
  }
}
