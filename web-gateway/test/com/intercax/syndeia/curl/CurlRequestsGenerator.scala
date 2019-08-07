package com.intercax.syndeia.curl

import java.net.URL

trait CurlRequestsGenerator {

  def getPostRequest(implicit authHeader: (String, String)): String

  def getPutRequest(implicit authHeader: (String, String)): String

  def getGetRequests(implicit authHeader: (String, String)): Seq[String]

  def getAllRequests(implicit authHeader: (String, String)): Seq[String]

  def GET(url: String)(implicit authHeader: (String, String)): String = {
    val headerStr = getHeader(authHeader)
    val netUrl = new URL(url)

    val urlStr = if (netUrl.getQuery().isEmpty) url else "'" + url + "'"

    s"""
       |curl --request GET \\
       |  --url $urlStr \\
       |  --header $headerStr
     """.stripMargin
  }

  def POST(url: String, body: String)(implicit authHeader: (String, String)): String = {
    val bodyWithQuotes = "'" + body + "'"
    val headerStr = getHeader(authHeader)

    s"""
       |curl --request POST \\
       |  --url $url \\
       |  --header 'Content-Type: application/json' \\
       |  --header $headerStr \\
       |  --data $bodyWithQuotes
         """.stripMargin
  }

  def PUT(url: String, body: String)(implicit authHeader: (String, String)): String = {
    val bodyWithQuotes = "'" + body + "'"
    val headerStr = getHeader(authHeader)

    s"""
       |curl --request PUT \\
       |  --url $url \\
       |  --header 'Content-Type: application/json' \\
       |  --header $headerStr \\
       |  --data $bodyWithQuotes
         """.stripMargin
  }

  def getUrl(serverName: String = "localhost", path: String = "", port: Int = 9000): String = {
    if (path.trim.isEmpty) s"http://$serverName:$port"
    else s"http://$serverName:$port/$path"
  }

  private def getHeader(authHeader: (String, String)): String = {
    val (key, value) = authHeader
    val headerStr = s"$key: $value"
    "'" + headerStr + "'"
  }
}
