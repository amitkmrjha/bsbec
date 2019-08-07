package com.intercax.syndeia.curl.container

import com.intercax.syndeia.curl.CurlRequestsGenerator

object ContainerRequestsGenerator extends CurlRequestsGenerator {

  val baseUrl = "http://localhost:9000/containers"

  override def getPostRequest(implicit authToken: (String, String)): String = {
    val body =
      s"""
       |{
       |    "name":"Container1",
       |    "keys": {
       |    	"key": "Key 1"
       |    },
       |    "type": {
       |        "key":"TypeA1"
       |    },
       |    "repository": {
       |        "key":"REPO1"
       |    }
       |}
     """.stripMargin

    POST(baseUrl, body)
  }

  override def getPutRequest(implicit authToken: (String, String)): String = {
    ""
  }

  override def getGetRequests(implicit authToken: (String, String)): Seq[String] = {
    Seq(getContainerByKey)
  }

  override def getAllRequests(implicit authToken: (String, String)): Seq[String] = {
    Seq(getPostRequest) ++ getGetRequests
  }

  private def getContainerByKey(implicit authToken: (String, String)): String = {
    GET(s"$baseUrl/KEY%202")
  }
}
