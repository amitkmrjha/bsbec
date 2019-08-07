package com.amit.exercise.impl.table

import java.util

import akka.Done
import com.amit.exercise.Entity
import com.datastax.driver.core.querybuilder.{Delete, Insert}
import com.datastax.driver.core.{BoundStatement, PreparedStatement}
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraSession
import play.api.Logger

import scala.concurrent.{ExecutionContext, Future, Promise}

trait EntityTable[T <: Entity] {
  private val logger = Logger(this.getClass)

  protected val insertPromise = Promise[PreparedStatement]

  protected val deletePromise = Promise[PreparedStatement]

  protected def tableName: String

  protected def primaryKey: String

  protected def tableScript: String

  protected def fields: Seq[String]

  protected def prepareDelete : Delete.Where

  protected def getDeleteBindValues(entity: T): Seq[AnyRef]

  protected def cL: util.List[String]

  protected def vL: util.List[AnyRef]

  protected def prepareInsert : Insert

  protected def  getInsertBindValues(entity: T): Seq[AnyRef]

  protected def getAllQueryString: String

  def insert(t:T)
            (implicit session: CassandraSession, ec: ExecutionContext):Future[BoundStatement] =  {
    val bindV = getInsertBindValues(t)
    bindPrepare(insertPromise,bindV)
  }

  def insert(ts:Seq[T])
            (implicit session: CassandraSession, ec: ExecutionContext):Future[Seq[BoundStatement]] =  {
    val seqF = ts.map{t=>
      val bindV = getInsertBindValues(t)
      bindPrepare(insertPromise,bindV)
    }
    Future.sequence(seqF)
  }

  def delete(t:T)
            (implicit session: CassandraSession, ec: ExecutionContext):Future[Option[BoundStatement]] ={
    val bindV = getDeleteBindValues(t)
    bindPrepare(deletePromise,bindV).map(x => Some(x))
  }

  def createTable()
                 (implicit session: CassandraSession, ec: ExecutionContext):Future[Done] = {
    for {
      _ <- sessionExecuteCreateTable(tableScript)
    }yield Done
  }

  protected def sessionExecuteCreateTable(tableScript: String)
                                         (implicit session: CassandraSession, ec: ExecutionContext) : Future[Done] = {
    session.executeCreateTable(tableScript).recover {
      case ex: Exception =>
        logger.error(s"Store MS CreateTable ${tableScript} execute error => ${ex.getMessage}", ex)
        throw ex
    }
  }

  def prepareStatement()
                      (implicit session: CassandraSession, ec: ExecutionContext):Future[Done] = {
    val insertRepositoryFuture = sessionPrepare(prepareInsert.toString)
    insertPromise.completeWith(insertRepositoryFuture)
    val deleteRepositoryFuture = sessionPrepare(prepareDelete.toString)
    deletePromise.completeWith(deleteRepositoryFuture)
    for {
      _ <- insertRepositoryFuture
      _ <- deleteRepositoryFuture
    }yield Done
  }

  protected def sessionPrepare(stmt: String)
                              (implicit session: CassandraSession, ec: ExecutionContext):Future[PreparedStatement] = {
    session.prepare(stmt).recover{
      case ex:Exception =>
        logger.error(s"Statement ${stmt} prepare error => ${ex.getMessage}",ex)
        throw ex
    }
  }

  protected def bindPrepare(ps:Promise[PreparedStatement],bindV:Seq[AnyRef])(implicit session: CassandraSession, ec: ExecutionContext):Future[BoundStatement] = {
    ps.future.map(x =>
      try {
        x.bind(bindV: _*)
      }catch{
        case ex:Exception =>
          logger.error(s"bindPrepare ${x.getQueryString} => ${ex.getMessage}", ex)
          throw ex
      }
    )
  }
}
