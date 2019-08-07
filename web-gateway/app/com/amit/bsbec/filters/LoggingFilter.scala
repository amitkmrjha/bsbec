package com.intercax.syndeia.filters

import javax.inject.Inject

import akka.stream.Materializer
import org.joda.time.format.{DateTimeFormatter, ISODateTimeFormat}
import play.api.Logger
import play.api.i18n.{Lang, Langs, MessagesApi}
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class LoggingFilter(messagesApi: MessagesApi, languages: Langs) (implicit val mat: Materializer, ec: ExecutionContext) extends Filter {

  implicit  val lang :Lang = languages.availables.head

  def apply(nextFilter: RequestHeader => Future[Result])
           (requestHeader: RequestHeader): Future[Result] = {
    val accessLog = Logger("access")
    val logger = Logger(this.getClass)
    val startTime = System.currentTimeMillis
    val fmt: DateTimeFormatter = ISODateTimeFormat.dateTime()

    nextFilter(requestHeader).map { result =>
      val endTime = System.currentTimeMillis
      val requestTime = endTime - startTime
      val elapsedMillis = endTime - startTime
      val timeStamp = fmt.print(startTime)

      val remoteIpAddress: String = {
        // see http://johannburkard.de/blog/programming/java/x-forwarded-for-http-header.html
          requestHeader.headers.get("X-Forwarded-For").map(_.split(",").head).getOrElse(
          requestHeader.headers.get("Remote_Addr").getOrElse(requestHeader.remoteAddress))
      }
      val userName = requestHeader.headers.get("REMOTE_USER").getOrElse("-")
      val requestSummary = {
        val query = if (requestHeader.rawQueryString.nonEmpty) s"?${ requestHeader.rawQueryString }" else ""
        val protocol = requestHeader.headers.get("Content-Length").getOrElse("-")
        val httpVersion = requestHeader.version
        s"${ requestHeader.method } ${ requestHeader.path }$query $protocol $httpVersion"
      }
      val statusCode = result.header.status
      val contentLength = requestHeader.headers.get("Content-Length").getOrElse("-")
      val referrer = requestHeader.headers.get("referer").getOrElse("-")
      val userAgent = requestHeader.headers.get("User-Agent").getOrElse("-")

      //accessLog.info(s"method=${requestHeader.method} uri=${requestHeader.uri} remote-address=${requestHeader.remoteAddress}" +s" status=${result.header.status}")
      //val acessLogEntry = s"""$remoteIpAddress - $userName $timeStamp "$requestSummary" $statusCode $contentLength $referrer "$userAgent" $elapsedMillis"""
      val acessLogEntry = s"""$remoteIpAddress - $userName "$requestSummary" $statusCode $contentLength $referrer "$userAgent" $elapsedMillis"""
      //accessLog.info(acessLogEntry)

      logger.info(messagesApi("filter.requestheader.info",requestHeader.id,requestHeader.method,requestHeader.uri,
        requestTime,result.header.status))
      //logger.info(s"${requestHeader.method} ${requestHeader.uri} took ${requestTime}ms and returned ${result.header.status}")
      result.withHeaders("Request-Time" -> requestTime.toString)
    }
  }
}
