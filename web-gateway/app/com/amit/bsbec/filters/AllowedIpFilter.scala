package com.amit.bsbec.filters

import java.net.InetAddress

import akka.stream.Materializer
import com.amit.exercise.api.ExerciseService
import javax.inject._
import play.api.Logger
import play.api.http.{DefaultHttpFilters, HttpErrorHandler, Status}
import play.api.mvc._
import play.api.libs.streams.Accumulator
import play.api.MarkerContexts.SecurityMarkerContext
import play.api.cache.AsyncCacheApi
import play.api.libs.json.Json

import scala.concurrent.{ExecutionContext, Future}

class AllowedIpFilter (cache: AsyncCacheApi,exerciseService: ExerciseService,errorHandler: HttpErrorHandler)(implicit  ec: ExecutionContext,val mat: Materializer) extends Filter {

  val logger = Logger(this.getClass)

  def apply(nextFilter: RequestHeader => Future[Result])(requestHeader: RequestHeader): Future[Result] = {
    val remoteIp = remoteIpAddress(requestHeader)

    isValidIP(remoteIp).flatMap{
      case true => nextFilter(requestHeader)
      case false =>   errorHandler.onClientError(requestHeader, Status.BAD_REQUEST, s"Ip not allowed: ${remoteIp}")
    }
  }

  private def isValidIP(dotIp: String): Future[Boolean] = cache.getOrElseUpdate[Boolean](dotIp) {
    val longIp = IPv4ToLong(dotIp)
    exerciseService.isBlackListIp(longIp).invoke.map{
      case false => true
      case true => false
    }.recover{
      case ex:Exception =>
        logger.debug(s"AllowedIpFilter isBlackListIp exception ${ex.getMessage}")
        true
    }
  }

  private def remoteIpAddress(request: RequestHeader):String = {
    //request.headers.get("x-forwarded-for").getOrElse(request.remoteAddress.toString)
    // see http://johannburkard.de/blog/programming/java/x-forwarded-for-http-header.html
    request.headers.get("X-Forwarded-For").map(_.split(",").head).getOrElse(
      request.headers.get("Remote_Addr").getOrElse(request.remoteAddress))
  }

  private def IPv4ToLong(dottedIP: String): Long = {
    val addrArray: Array[String] = dottedIP.split("\\.")
    var num: Long = 0
    var i: Int = 0
    while (i < addrArray.length) {
      val power: Int = 3 - i
      num = num + ((addrArray(i).toInt % 256) * Math.pow(256, power)).toLong
      i += 1
    }
    num
  }

  private def LongToIPv4 (ip : Long) : String = {
    val bytes: Array[Byte] = new Array[Byte](4)
    bytes(0) = ((ip & 0xff000000) >> 24).toByte
    bytes(1) = ((ip & 0x00ff0000) >> 16).toByte
    bytes(2) = ((ip & 0x0000ff00) >> 8).toByte
    bytes(3) = (ip & 0x000000ff).toByte
    InetAddress.getByAddress(bytes).getHostAddress()
  }
}
