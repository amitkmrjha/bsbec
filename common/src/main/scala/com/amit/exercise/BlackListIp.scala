package com.amit.exercise

import java.util.UUID

import play.api.libs.json.{Format, Json}

case class BlackListIp(id:Option[UUID], ip:Long) extends Entity
object BlackListIp {
  implicit val appHostIpFormat: Format[BlackListIp] = Json.format[BlackListIp]
}
