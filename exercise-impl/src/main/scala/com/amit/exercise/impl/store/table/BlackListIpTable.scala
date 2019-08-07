package com.amit.exercise.impl.table

import java.util

import com.amit.exercise.BlackListIp
import com.amit.exercise.impl.store.store.{ColumnFamilies, Columns}
import com.datastax.driver.core.querybuilder.{Delete, Insert, QueryBuilder}

import scala.collection.JavaConverters._
import com.datastax.driver.core.querybuilder.QueryBuilder
import com.datastax.driver.core.utils.UUIDs

object BlackListIpTable extends EntityTable[BlackListIp]{

  override protected def tableScript: String =
    s"""
     CREATE TABLE IF NOT EXISTS ${tableName} (
         ${Columns.Id} timeuuid,
         ${Columns.Ip}  bigint,
         PRIMARY KEY (${primaryKey})
         )
      """.stripMargin

  override protected def fields: Seq[String]  = Seq(
    Columns.Id,
    Columns.Ip,
  )

  override protected def cL: util.List[String] = fields.toList.asJava

  override protected def vL: util.List[AnyRef] = fields.map(_ =>
    QueryBuilder.bindMarker().asInstanceOf[AnyRef]).toList.asJava

  override protected def prepareInsert: Insert  = QueryBuilder.insertInto(tableName).values(cL, vL)

  override protected def prepareDelete: Delete.Where = QueryBuilder.delete().from(tableName)
    .where(QueryBuilder.eq(Columns.Ip,QueryBuilder.bindMarker()))

  override protected def getDeleteBindValues(entity: BlackListIp): Seq[AnyRef] = {
    val bindValues: Seq[AnyRef] = Seq(entity.ip.asInstanceOf[java.lang.Long])
    bindValues
  }
  override protected def getInsertBindValues(entity: BlackListIp): Seq[AnyRef] = {
    val bindValues: Seq[AnyRef] = fields.map(x => x match {
      case Columns.Id => entity.id.getOrElse(UUIDs.timeBased())
      case Columns.Ip => entity.ip.asInstanceOf[java.lang.Long]
    })
    bindValues
  }

  override  def getAllQueryString: String =  {
    val select = QueryBuilder.select().from(tableName)
    select.toString
  }

  def byIp(ip: Long): String = {
    val select = QueryBuilder.select().from(tableName)
      .where(QueryBuilder.eq(Columns.Ip, ip))
    select.toString
  }

  def deleteByAppId (ip: Long): String = {
    val select = QueryBuilder.delete().from(tableName)
      .where(QueryBuilder.eq(Columns.Ip, ip))
    select.toString
  }

  override protected def tableName: String = ColumnFamilies.BlackListIp

  override protected def primaryKey: String = s"${Columns.Ip}"
}
