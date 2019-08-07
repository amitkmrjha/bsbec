package com.amit.exercise

import java.time.LocalDateTime
import java.util.UUID

import play.api.libs.json.{Format, Json}

case class Category(id:Option[UUID], `type`:String, title:String, keywords:Seq[String], creation_date:LocalDateTime) extends Entity
object Category {
  implicit val categoryFormat: Format[Category] = Json.format[Category]
}
