package com.amit.exercise.impl.table

import java.time.LocalDateTime
import java.util

import com.amit.exercise.Category
import com.amit.exercise.impl.store.store.{ColumnFamilies, Columns}
import com.amit.exercise.util.LocalDateTimeConverters._
import com.datastax.driver.core.querybuilder.{Delete, Insert, QueryBuilder}

import scala.collection.JavaConverters._
import com.datastax.driver.core.querybuilder.QueryBuilder
import com.datastax.driver.core.utils.UUIDs

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

