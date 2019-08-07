package com.amit.exercise.impl.daos

import com.amit.exercise.Entity
import java.util.UUID

import akka.Done
import com.amit.exercise.impl.store.store.Columns
import com.datastax.driver.core.Row
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraSession

import scala.concurrent.{ExecutionContext, Future}

trait EntityDao[T <: Entity] {

  protected def convert(r: Row): T

  protected def sessionSelectAll(queryString: String): Future[Seq[T]]

  protected def sessionSelectOne(queryString: String): Future[Option[T]]

  protected def id(r: Row):Option[UUID] = Option(r.getUUID(Columns.Id))

  protected def createSequentially(seq: Seq[T])(f: T => Future[T])(implicit ec:ExecutionContext): Future[Seq[T]] =
    seq.foldLeft(Future.successful(Seq.empty[T])) {
      case (acc, nxt) => acc.flatMap(bs => f(nxt).map(b => bs :+ b))
    }
}
