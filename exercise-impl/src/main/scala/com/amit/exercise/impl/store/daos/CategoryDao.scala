package com.amit.exercise.impl.store.daos

import java.time.LocalDateTime
import java.util.UUID

import akka.Done
import com.amit.exercise.Category
import com.amit.exercise.impl.daos.EntityDao
import com.amit.exercise.impl.store.store.Columns
import com.amit.exercise.impl.table.CategoryTable
import com.datastax.driver.core.Row
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraSession
import com.amit.exercise.util.LocalDateTimeConverters._
import play.api.Logger

import scala.collection.JavaConverters._
import scala.concurrent.{ExecutionContext, Future}

abstract class AbstractCategoryDao[T <:Category](session: CassandraSession)(implicit ec: ExecutionContext) extends EntityDao[T]{

  override protected def sessionSelectAll(queryString: String): Future[Seq[T]] = {
    session.selectAll(queryString).map(_.map(convert))
  }

  override protected def sessionSelectOne(queryString: String): Future[Option[T]] = {
    session.selectOne(queryString).map(_.map(convert))
  }

  protected def `type`(r: Row):Option[String] = Option(r.getString(Columns.Type))

  protected def title(r: Row):String = r.getString(Columns.Title)

  protected def keyWords(r: Row):Seq[String] = r.getSet(Columns.KeyWords, classOf[String]).asScala.toSeq

  protected def creationDate(r: Row):Option[LocalDateTime]= Option(r.getTimestamp(Columns.CreationDate).toLocalDateTime)

}


class CategoryDao(session: CassandraSession)(implicit ec: ExecutionContext) extends AbstractCategoryDao[Category](session){

  val logger = Logger(this.getClass)

  val lazyPrepare = CategoryTable.prepareStatement()(session,ec).map(x=>
    logger.debug(s"Category Prepare statement ${x}")
  )

  def getByTitle(title: String): Future[Option[Category]] = {
    sessionSelectOne(CategoryTable.queryByTitle(title))
  }

  override protected def convert(r: Row): Category = {
    Category(id(r), `type`(r), title(r), keyWords(r), creationDate(r))
  }

  def delete(title: String): Future[Done] = {
    session.executeWrite(CategoryTable.deleteByTitle(title))
  }

  def create(category:Category): Future[Category] = {
    CategoryTable.insert(category)(session,ec).flatMap{
      case Some(bs) =>  session.executeWrite(bs).map(_ => category)
      case None => throw new Exception(s"Unable to prepare insert statement for ${category}")
    }
  }
}