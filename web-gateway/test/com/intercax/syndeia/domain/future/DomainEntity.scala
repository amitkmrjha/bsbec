package com.intercax.syndeia.domain.future

import java.time.LocalDateTime

import com.intercax.syndeia.domain.references.{TypeReference, UserReference}

trait DomainObject

trait DomainEntity extends DomainObject {
  def name: String

  def description: String

  def createdBy: UserReference

  def createdDate: LocalDateTime

  def modifiedBy: UserReference

  def modifiedDate: LocalDateTime

  def otherInfo: Map[String, String]
}

trait TypeEntity extends DomainEntity {
  def `type`: TypeReference
}

trait VersionedEntity extends TypeEntity {
  def version: String
}
