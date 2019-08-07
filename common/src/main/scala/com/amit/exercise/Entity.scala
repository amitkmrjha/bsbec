package com.amit.exercise

import java.util.UUID

import play.api.libs.json.{Format, Json}

trait Entity {
  def id: Option[UUID]
}





