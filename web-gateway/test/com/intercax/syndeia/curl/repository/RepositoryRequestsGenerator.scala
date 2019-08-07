package com.intercax.syndeia.curl.repository

import com.intercax.syndeia.curl.CurlRequestsGenerator

object RepositoryRequestsGenerator extends CurlRequestsGenerator {

  val baseUrl = "http://localhost:9000/repositories"

  override def getPostRequest(implicit authHeader: (String, String)): String = {
    val body =
      s"""
         |{
         |	"name": "Repo 1",
         |	"host": "http://a.com:9000",
         |	"type": {
         |		"key": "repo type1"
         |	},
         |    "attributes": {
         |        "weight": {
         |            "clazz": "DoubleValue",
         |            "value": 2.2,
         |            "unit": "kg"
         |        }
         |    }
         |}
     """.stripMargin

    POST(baseUrl, body)
  }

  override def getPutRequest(implicit authHeader: (String, String)): String = {
    ""
  }

  override def getGetRequests(implicit authHeader: (String, String)): Seq[String] = {
    Seq(getRepositoryByKey)
  }

  override def getAllRequests(implicit authHeader: (String, String)): Seq[String] = {
    Seq(getPostRequest) ++ getGetRequests
  }

  private def getRepositoryByKey(implicit authHeader: (String, String)): String = {
    GET(s"$baseUrl/REPO1")
  }
}
