package com.amit.exercise.impl.store.daos

import akka.Done
import com.amit.exercise.BankInfo
import com.amit.exercise.impl.daos.EntityDao
import com.amit.exercise.impl.store.store.Columns
import com.amit.exercise.impl.table.BankInfoTable
import com.datastax.driver.core.{BoundStatement, Row}
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraSession
import play.api.Logger

import scala.concurrent.{ExecutionContext, Future}

abstract class AbstractBankInfoDao[T <:BankInfo](session: CassandraSession)(implicit ec: ExecutionContext) extends EntityDao[T] {


  override protected def sessionSelectAll(queryString: String): Future[Seq[T]] = {
    session.selectAll(queryString).map(_.map(convert))
  }

  override protected def sessionSelectOne(queryString: String): Future[Option[T]] = {
    session.selectOne(queryString).map(_.map(convert))
  }

  protected def name(r: Row):String = r.getString(Columns.Name)

  protected def identifier(r: Row):String = r.getString(Columns.Identifier)
}

class BankInfoDao(session: CassandraSession)(implicit ec: ExecutionContext) extends AbstractBankInfoDao[BankInfo](session){

  val logger = Logger(this.getClass)

  val lazyPrepare = BankInfoTable.prepareStatement()(session,ec).map(x=>
    logger.debug(s"Bank info Prepare statement ${x}")
  )

  def getByIdentifier(identifier: String): Future[Option[BankInfo]] = {
    sessionSelectOne(BankInfoTable.queryByIdentifier(identifier))
  }

  override protected def convert(r: Row): BankInfo = {
    BankInfo(id(r), name(r), identifier(r))
  }

  def deleteByIdentifier(identifier: String): Future[Done] = {
    session.executeWrite(BankInfoTable.deleteByIdentifier(identifier))
  }

  def create(bankInfo:BankInfo): Future[BankInfo] = {
    BankInfoTable.insert(bankInfo)(session,ec).flatMap{ bs => session.executeWrite(bs).map(_ => bankInfo)}
  }
}

