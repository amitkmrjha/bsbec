package com.amit.exercise.impl.table

import java.util

import com.amit.exercise.BankInfo
import com.amit.exercise.impl.store.store.{ColumnFamilies, Columns}
import com.datastax.driver.core.querybuilder.{Delete, Insert, QueryBuilder}

import scala.collection.JavaConverters._
import com.datastax.driver.core.querybuilder.QueryBuilder
import com.datastax.driver.core.utils.UUIDs
import play.api.Logger

object BankInfoTable extends EntityTable[BankInfo]{

  val logger = Logger(this.getClass)

  override protected def tableScript: String =
    s"""
     CREATE TABLE IF NOT EXISTS ${tableName} (
         ${Columns.Id} timeuuid,
         ${Columns.Name}  text,
       |${Columns.Identifier}  text,
         PRIMARY KEY (${primaryKey})
         )
      """.stripMargin

  override protected def fields: Seq[String]  = Seq(
    Columns.Id,
    Columns.Name,
    Columns.Identifier
  )

  override protected def cL: util.List[String] = fields.toList.asJava

  override protected def vL: util.List[AnyRef] = fields.map(_ =>
    QueryBuilder.bindMarker().asInstanceOf[AnyRef]).toList.asJava

  override protected def prepareInsert: Insert  = QueryBuilder.insertInto(tableName).values(cL, vL)

  override protected def prepareDelete: Delete.Where = QueryBuilder.delete().from(tableName)
    .where(QueryBuilder.eq(Columns.Identifier,QueryBuilder.bindMarker()))

  override protected def getDeleteBindValues(entity: BankInfo): Seq[AnyRef] = {
    val bindValues: Seq[AnyRef] = Seq(entity.identifier)
    bindValues
  }
  override protected def getInsertBindValues(entity: BankInfo): Seq[AnyRef] = {
    val bindValues: Seq[AnyRef] = fields.map(x => x match {
      case Columns.Id => entity.id.getOrElse(UUIDs.timeBased())
      case Columns.Name => entity.name
      case Columns.Identifier => entity.identifier
    })
    bindValues
  }

  override  def getAllQueryString: String =  {
    val select = QueryBuilder.select().from(tableName)
    select.toString
  }

  def queryByIdentifier(identifier: String): String = {
    val select = QueryBuilder.select().from(tableName)
      .where(QueryBuilder.eq(Columns.Identifier, identifier))
    select.toString
  }

  def deleteByIdentifier (identifier: String): String = {
    val select = QueryBuilder.delete().from(tableName)
      .where(QueryBuilder.eq(Columns.Identifier, identifier))
    select.toString
  }

  override protected def tableName: String = ColumnFamilies.BankInfo

  override protected def primaryKey: String = s"${Columns.Identifier},${Columns.Name}"
}
