package com.amit.exercise

import java.time.LocalDateTime
import java.util.UUID

import play.api.libs.json.{Format, Json}

case class Category(id: Option[UUID], `type`: Option[String], title: String, keywords: Seq[String], creation_date: Option[LocalDateTime]) extends Entity
object Category {
  implicit val categoryFormat: Format[Category] = Json.format[Category]
}

case class KeyWordTitle(id: Option[UUID], title: String, keyword: String) extends Entity
object KeyWordTitle {
  implicit val keyWordCategoryFormat: Format[KeyWordTitle] = Json.format[KeyWordTitle]
}
