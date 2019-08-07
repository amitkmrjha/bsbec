package com.amit.exercise.impl.table

import java.time.LocalDateTime
import java.util

import com.amit.exercise.{Category, KeyWordTitle}
import com.amit.exercise.impl.store.store.{ColumnFamilies, Columns}
import com.amit.exercise.util.LocalDateTimeConverters._
import com.datastax.driver.core.BoundStatement
import com.datastax.driver.core.querybuilder.{Delete, Insert, QueryBuilder}

import scala.collection.JavaConverters._
import com.datastax.driver.core.querybuilder.QueryBuilder
import com.datastax.driver.core.utils.UUIDs
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraSession

import scala.concurrent.{ExecutionContext, Future}

object CategoryTable extends EntityTable[Category] {
  override protected def tableScript: String =
    s"""
     CREATE TABLE IF NOT EXISTS ${tableName} (
         ${Columns.Id} timeuuid,
         ${Columns.Type}  text,
         ${Columns.Title}  text,
         ${Columns.KeyWords}  set<text>,
         ${Columns.CreationDate}  timestamp,
         PRIMARY KEY (${primaryKey})
         )
      """.stripMargin

  override protected def fields: Seq[String]  = Seq(
    Columns.Id,
    Columns.Type,
    Columns.Title,
    Columns.KeyWords,
    Columns.CreationDate
  )

  override protected def cL: util.List[String] = fields.toList.asJava

  override protected def vL: util.List[AnyRef] = fields.map(_ =>
    QueryBuilder.bindMarker().asInstanceOf[AnyRef]).toList.asJava

  override protected def prepareInsert: Insert  = QueryBuilder.insertInto(tableName).values(cL, vL)

  override protected def prepareDelete: Delete.Where = QueryBuilder.delete().from(tableName)
    .where(QueryBuilder.eq(Columns.Title,QueryBuilder.bindMarker()))

  override protected def getDeleteBindValues(entity: Category): Seq[AnyRef] = {
    val bindValues: Seq[AnyRef] = Seq(entity.title)
    bindValues
  }
  override protected def getInsertBindValues(entity: Category): Seq[AnyRef] = {
    val bindValues: Seq[AnyRef] = fields.map(x => x match {
      case Columns.Id => entity.id.getOrElse(UUIDs.timeBased())
      case Columns.Type => entity.`type`.getOrElse("category")
      case Columns.Title => entity.title
      case Columns.KeyWords => entity.keywords.map(x => x.toString).toSet.asJava
      case Columns.CreationDate => entity.creation_date.getOrElse(LocalDateTime.now()).toDate
    })
    bindValues
  }

  override  def getAllQueryString: String =  {
    val select = QueryBuilder.select().from(tableName)
    select.toString
  }

  def queryByTitle(title: String): String = {
    val select = QueryBuilder.select().from(tableName)
        .where(QueryBuilder.eq(Columns.Title,title))
    select.toString
  }

  def deleteByTitle(title: String): String = {
    val select = QueryBuilder.delete().from(tableName)
      .where(QueryBuilder.eq(Columns.Title,title))
    select.toString
  }


  override protected def tableName: String = ColumnFamilies.Category

  override protected def primaryKey: String = s"${Columns.Title}"
}

object KeyWordTitleTable extends EntityTable[KeyWordTitle] {
  override protected def tableScript: String =
    s"""
     CREATE TABLE IF NOT EXISTS ${tableName} (
         ${Columns.Id} timeuuid,
         ${Columns.KeyWord}  text,
         ${Columns.Title}  timestamp,
         PRIMARY KEY (${primaryKey})
         )
      """.stripMargin

  override protected def fields: Seq[String]  = Seq(
    Columns.Id,
    Columns.KeyWord,
    Columns.Title
  )

  override protected def cL: util.List[String] = fields.toList.asJava

  override protected def vL: util.List[AnyRef] = fields.map(_ =>
    QueryBuilder.bindMarker().asInstanceOf[AnyRef]).toList.asJava

  override protected def prepareInsert: Insert  = QueryBuilder.insertInto(tableName).values(cL, vL)

  override protected def prepareDelete: Delete.Where = QueryBuilder.delete().from(tableName)
    .where(QueryBuilder.eq(Columns.KeyWord,QueryBuilder.bindMarker()))

  override protected def getDeleteBindValues(entity: KeyWordTitle): Seq[AnyRef] = {
    val bindValues: Seq[AnyRef] = Seq(entity.keyword)
    bindValues
  }
  override protected def getInsertBindValues(entity: KeyWordTitle): Seq[AnyRef] = {
    val bindValues: Seq[AnyRef] = fields.map(x => x match {
      case Columns.Id => entity.id.getOrElse(UUIDs.timeBased())
      case Columns.KeyWord => entity.keyword
      case Columns.Title => entity.title
    })
    bindValues
  }

  override  def getAllQueryString: String =  {
    val select = QueryBuilder.select().from(tableName)
    select.toString
  }

  def queryByKeyWord(keyWord: String): String = {
    val select = QueryBuilder.select().from(tableName)
      .where(QueryBuilder.eq(Columns.KeyWord,keyWord))
    select.toString
  }

  def queryByInKeyWords(keyWords: Seq[String]): String = {
    val select = QueryBuilder.select().from(tableName)
      .where(QueryBuilder.in(Columns.KeyWord,keyWords))
    select.toString
  }

  def deleteByTitle(title: String): String = {
    val select = QueryBuilder.delete().from(tableName)
      .where(QueryBuilder.eq(Columns.Title,title))
    select.toString
  }


  override protected def tableName: String = ColumnFamilies.KeyWordTitle

  override protected def primaryKey: String = s"${Columns.KeyWord}"

  def insertSeq(seqT:Seq[KeyWordTitle])
            (implicit session: CassandraSession, ec: ExecutionContext):Future[Seq[BoundStatement]] =  {
    val futureSeq = seqT.map{ t =>
      val bindV = getInsertBindValues(t)
      bindPrepare(insertPromise,bindV)
    }
    Future.sequence(futureSeq)
  }

  def deleteSeq(seqT:Seq[KeyWordTitle])
            (implicit session: CassandraSession, ec: ExecutionContext):Future[Seq[BoundStatement]] ={
    val futureSeq = seqT.map { t =>
      val bindV = getDeleteBindValues(t)
      bindPrepare(deletePromise,bindV)
    }
    Future.sequence(futureSeq)

  }

}






