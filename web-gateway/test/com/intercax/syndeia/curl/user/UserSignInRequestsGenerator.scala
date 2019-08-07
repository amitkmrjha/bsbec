package com.intercax.syndeia.curl.user

import com.intercax.syndeia.curl.CurlRequestsGenerator

object UserSignInRequestsGenerator extends CurlRequestsGenerator {

  val localhost = "localhost"
  val remote = "storeman.intercax.com"

  val localUrl = getUrl(localhost, "signIn")
  val remoteUrl = getUrl(remote, "signIn")

  override def getPostRequest(implicit authHeader: (String, String)): String = {
    val body =
      s"""
         |{
         |  "email": "MW@intercax.com",
         |  "password": "123",
         |  "rememberMe": false
         |}
       """.stripMargin

    POST(remoteUrl, body)
  }

  override def getPutRequest(implicit authHeader: (String, String)): String = ???

  override def getGetRequests(implicit authHeader: (String, String)): Seq[String] = ???

  override def getAllRequests(implicit authHeader: (String, String)): Seq[String] = ???
}
