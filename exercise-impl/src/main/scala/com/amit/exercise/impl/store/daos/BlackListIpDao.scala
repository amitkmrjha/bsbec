package com.amit.exercise.impl.store.daos

import akka.Done
import com.amit.exercise.BlackListIp
import com.amit.exercise.impl.daos.EntityDao
import com.amit.exercise.impl.store.store.Columns
import com.amit.exercise.impl.table.{BankInfoTable, BlackListIpTable}
import com.datastax.driver.core.Row
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraSession
import play.api.Logger

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

  val logger = Logger(this.getClass)

  val lazyPrepare = BlackListIpTable.prepareStatement()(session,ec).map(x=>
    logger.debug(s"Black List Ip Prepare statement ${x}")
  )

  def isBlackList(ip: Long): Future[Boolean] = {
    sessionSelectOne(BlackListIpTable.byIp(ip)).map{
      case Some(ip) => true
      case None => false
    }
  }

  override protected def convert(r: Row): BlackListIp = {
    BlackListIp(id(r),ip(r))
  }

  def delete(ip: Long): Future[Done] = {
    session.executeWrite(BlackListIpTable.deleteByAppId(ip))
  }

  def create(blackListIp: BlackListIp): Future[BlackListIp] = {
    BlackListIpTable.insert(blackListIp)(session,ec).flatMap{
      case Some(bs) =>  session.executeWrite(bs).map(_ => blackListIp)
      case None => throw new Exception(s"Unable to prepare insert statement for ${BlackListIp}")
    }
  }
}