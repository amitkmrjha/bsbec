package com.amit.exercise.impl.store.daos

import akka.Done
import com.amit.exercise.{BlackListIp}
import com.amit.exercise.impl.daos.EntityDao
import com.amit.exercise.impl.store.store.Columns
import com.amit.exercise.impl.table.BlackListIpTable
import com.datastax.driver.core.Row
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraSession

import scala.concurrent.{ExecutionContext, Future}

abstract class AbstractHostIpDao [T <:BlackListIp](session: CassandraSession)(implicit ec: ExecutionContext) extends EntityDao[T]{

  override protected def sessionSelectAll(queryString: String): Future[Seq[T]] = {
    session.selectAll(queryString).map(_.map(convert))
  }

  override protected def sessionSelectOne(queryString: String): Future[Option[T]] = {
    session.selectOne(queryString).map(_.map(convert))
  }

  protected def ip(r: Row):Long = r.getLong(Columns.Ip)
}


class BlackListIpDao (session: CassandraSession)(implicit ec: ExecutionContext) extends AbstractHostIpDao[BlackListIp](session){
  def getByIdentifier(ip: Long): Future[Option[BlackListIp]] = {
    sessionSelectOne(BlackListIpTable.byIp(ip))
  }

  override protected def convert(r: Row): BlackListIp = {
    BlackListIp(id(r),ip(r))
  }

  def deleteByTitle(ip: Long): Future[Done] = {
    session.executeWrite(BlackListIpTable.deleteByAppId(ip))
  }
}