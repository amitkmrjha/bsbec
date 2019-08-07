package com.intercax.syndeia.domain.future

import java.time.LocalDateTime

import com.intercax.syndeia.domain.TypeAliases.AttributeName
import com.intercax.syndeia.domain.primitives.Value
import com.intercax.syndeia.domain.references.{ArtifactTypeReference, ContainerReference, UserReference}

sealed trait Artifact extends VersionedEntity {
  def attributes: Map[AttributeName, Value]

  def customTypes: Seq[ArtifactTypeReference]

  def container: ContainerReference
}

final case class InternalArtifact(identity: InternalIdentity,
                                  version: String,
                                  name: String,
                                  description: String,
                                  createdBy: UserReference,
                                  createdDate: LocalDateTime,
                                  modifiedBy: UserReference,
                                  modifiedDate: LocalDateTime,
                                  otherInfo: Map[String, String],
                                  `type`: ArtifactTypeReference,
                                  attributes: Map[AttributeName, Value] = Map.empty,
                                  customTypes: Seq[ArtifactTypeReference] = Seq.empty,
                                  container: ContainerReference)
  extends Artifact

final case class ExternalArtifact(identity: InternalIdentity,
                                  externalIdentity: ExternalIdentity,
                                  version: String,
                                  name: String,
                                  description: String,
                                  createdBy: UserReference,
                                  createdDate: LocalDateTime,
                                  modifiedBy: UserReference,
                                  modifiedDate: LocalDateTime,
                                  otherInfo: Map[String, String],
                                  `type`: ArtifactTypeReference,
                                  attributes: Map[AttributeName, Value] = Map.empty,
                                  customTypes: Seq[ArtifactTypeReference] = Seq.empty,
                                  container: ContainerReference)
  extends Artifact
