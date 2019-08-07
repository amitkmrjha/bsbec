package com.intercax.syndeia.domain.future

import java.util.UUID

import play.api.libs.json.{Format, Json}

sealed trait Identity[T] {
  def id: T

  def key: String
}

final case class InternalIdentity(id: UUID, key: String)
  extends Identity[UUID]

object InternalIdentity {
  implicit val internalIdentityFormat: Format[InternalIdentity] = Json.format[InternalIdentity]
}

final case class ExternalIdentity(id: String, key: String)
  extends Identity[String]

object ExternalIdentity {
  implicit val externalIdentityFormat: Format[ExternalIdentity] = Json.format[ExternalIdentity]
}
