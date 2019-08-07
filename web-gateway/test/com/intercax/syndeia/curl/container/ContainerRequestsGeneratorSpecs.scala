package com.intercax.syndeia.curl.container

import org.scalatest.{Matchers, WordSpec}
import play.api.Logger

class ContainerRequestsGeneratorSpecs extends WordSpec with Matchers {

  val logger = Logger(this.getClass)

  val authToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxLUM2TW45WlNkTGJ1XC91RjFRRnVBK2t2UmswYjRrMUE2OUFMUUt0MXlVdTA0TFlodjRDMmQybUhOVjNkbkU2dURCWWt4eUtvT25aNDhBN2NBV1JhXC90dE5TSURLNjhDRjc1YWVZbHRRPT0iLCJpc3MiOiJTeW5kZWlhLUNsb3VkLXNpbGhvdWV0dGUiLCJwZXJtaXNzaW9uIjpbImFkbWluIiwiUkVQT1NJVE9SWV9DUkVBVEUiLCJSRUxBVElPTl9DUkVBVEUiLCJBUlRJRkFDVF9DUkVBVEUiXSwiZXhwIjoxNTM1NDg4Mzc2LCJpYXQiOjE1MzU0NDUxNzYsImp0aSI6IjA0MWViYTI0YWU4MDEwZTAwYTI2NWU5M2RiNDU3NmVjZDhkZDBmMTBiNDI0OGM4YzVhYzI3Y2Y3MzE0MDJiMDBjOTZmZGQ1ZmI4ZmQxZmU0ODJhYTI1YWVlNDRiYzM1N2M3OWQ3OWJkYzE2NmI1YTI1YTJkYmFkYWRkMmM5ZjY1YjM3ZDQ4OTI1MTFjYjk0MmEyOWNhM2U2YWM4YjgxMWQ2YmFjMjAzMjRiYTA4MjI2Mjk0OGE0NjM4YzVkZmI3YzdkOTRhOGRkODMxNTY3NjhjMzg0OTNiN2IzMTQ5NDQwYWU0NzRkMmNjMWY4ZTkyYjNlOWZhNTlkMTZkMTI0MDQifQ.bQFo9hoTMZHyLlwkUOUBNW5B7F2RMWryJT7QYX3djdM"
  implicit val authHeader: (String, String) = ("X-Auth-Token", authToken)

  "A ContainerRequestsGenerator" should {

    "generate curl commands for testing container endpoints" in {
      val allRequests = ContainerRequestsGenerator.getAllRequests
      allRequests should not be null
      allRequests should not be empty

      logger.debug(s"${allRequests.mkString(System.lineSeparator())}")
    }
  }
}
