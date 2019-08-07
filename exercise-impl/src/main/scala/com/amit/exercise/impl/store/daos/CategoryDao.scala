package com.amit.exercise.impl.store.daos

import java.time.LocalDateTime
import java.util.UUID

import akka.Done
import com.amit.exercise.{Category, KeyWordTitle}
import com.amit.exercise.impl.daos.EntityDao
import com.amit.exercise.impl.store.store.Columns
import com.amit.exercise.impl.table.{CategoryTable, KeyWordTitleTable}
import com.datastax.driver.core.{BatchStatement, BoundStatement, Row}
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraSession
import com.amit.exercise.util.LocalDateTimeConverters._
import com.datastax.driver.core.utils.UUIDs
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

  protected def keyWord(r: Row):String =  r.getString(Columns.KeyWord)

  protected def keyWords(r: Row):Seq[String] = r.getSet(Columns.KeyWords, classOf[String]).asScala.toSeq

  protected def creationDate(r: Row):Option[LocalDateTime]= Option(r.getTimestamp(Columns.CreationDate).toLocalDateTime)

}


class CategoryDao(session: CassandraSession)(implicit ec: ExecutionContext) extends AbstractCategoryDao[Category](session){

  val logger = Logger(this.getClass)

  for{
    ct <-  CategoryTable.prepareStatement()(session,ec)
    kt <-  KeyWordTitleTable.prepareStatement()(session,ec)
  }yield{
    logger.debug(s"Category Prepare statement ${ct}")
    logger.debug(s"KeyWordTitle Prepare statement ${kt}")
  }

  def getByTitle(title: String): Future[Option[Category]] = {
    sessionSelectOne(CategoryTable.queryByTitle(title))
  }

  override protected def convert(r: Row): Category = {
    Category(id(r), `type`(r), title(r), keyWords(r), creationDate(r))
  }

  def delete(title: String): Future[Done] = {
    getByTitle(title).flatMap{
      case Some(c) => deleteBaseAndView(c)
      case None => throw new Exception(s"No category found for title ${title} ")
    }
  }

  def create(category:Category): Future[Category] = {
    insertBaseAndView(category).map(_ => category)
  }

  protected def createKeyWordTitle(category:Category):Future[Done] = {
    KeyWordTitleTable.insertSeq( toCreateKeyWordTitle(category))(session,ec).flatMap{bsSeq =>
      executeBatch(bsSeq)
    }
  }

  protected def toCreateKeyWordTitle(category:Category):Seq[KeyWordTitle]= {
    category.keywords.map(x => KeyWordTitle(Option(UUIDs.timeBased()),category.title,x))
  }

  private def executeBatch(statements: Seq[BoundStatement]): Future[Done] = {
    val batch = new BatchStatement
    // statements is never empty, there is at least the store offset statement
    // for simplicity we just use batch api (even if there is only one)
    batch.addAll(statements.asJava)
    session.executeWriteBatch(batch)
  }

  private def insertBaseAndView(category:Category):Future[Done] = {
    for{
      basePS <- CategoryTable.insert(category)(session,ec)
      baseInsert <- session.executeWrite(basePS)
      ktView <- KeyWordTitleTable.insertSeq( toCreateKeyWordTitle(category))(session,ec)
      ktViewInsert <- executeBatch(ktView)
    }yield{
      Done.getInstance()
    }
  }

  private def deleteBaseAndView(category:Category):Future[Done] = {
    for{
      basePS <- CategoryTable.delete(category)(session,ec)
      base <- session.executeWrite(basePS)
      ktViewPS <- KeyWordTitleTable.deleteSeq( toCreateKeyWordTitle(category))(session,ec)
      ktView <- executeBatch(ktViewPS)
    }yield{
      Done.getInstance()
    }
  }


}