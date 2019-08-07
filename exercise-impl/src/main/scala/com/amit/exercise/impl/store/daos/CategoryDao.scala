package com.amit.exercise.impl.store.daos

import java.time.LocalDateTime
import java.util.UUID

import akka.Done
import com.amit.exercise.{Category}
import com.amit.exercise.impl.daos.EntityDao
import com.amit.exercise.impl.store.store.Columns
import com.amit.exercise.impl.table.CategoryTable
import com.datastax.driver.core.Row
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraSession
import com.amit.exercise.util.LocalDateTimeConverters._
import scala.collection.JavaConverters._
import scala.concurrent.{ExecutionContext, Future}

abstract class AbstractCategoryDao[T <:Category](session: CassandraSession)(implicit ec: ExecutionContext) extends EntityDao[T]{

  override protected def sessionSelectAll(queryString: String): Future[Seq[T]] = {
    session.selectAll(queryString).map(_.map(convert))
  }

  override protected def sessionSelectOne(queryString: String): Future[Option[T]] = {
    session.selectOne(queryString).map(_.map(convert))
  }

  protected def `type`(r: Row):String = r.getString(Columns.Type)

  protected def title(r: Row):String = r.getString(Columns.Title)

  protected def keyWords(r: Row):Seq[String] = r.getSet(Columns.KeyWords, classOf[String]).asScala.toSeq

  protected def creationDate(r: Row):LocalDateTime= r.getTimestamp(Columns.CreationDate).toLocalDateTime

}


class CategoryDao(session: CassandraSession)(implicit ec: ExecutionContext) extends AbstractCategoryDao[Category](session){

  def getByTitle(title: String): Future[Option[Category]] = {
    sessionSelectOne(CategoryTable.queryByTitle(title))
  }

  override protected def convert(r: Row): Category = {
    Category(id(r),`type`(r),title(r),keyWords(r),creationDate(r))
  }

  def deleteByTitle(title: String): Future[Done] = {
    session.executeWrite(CategoryTable.deleteByTitle(title))
  }
}