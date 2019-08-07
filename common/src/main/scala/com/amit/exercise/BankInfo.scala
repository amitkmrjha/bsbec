package com.amit.exercise

import java.util.UUID

import play.api.libs.json.{Format, Json}

case class BankInfo(id: Option[UUID], name: String, identifier: String) extends Entity
object BankInfo {
  implicit val bankInfoFormat: Format[BankInfo] = Json.format[BankInfo]
}
