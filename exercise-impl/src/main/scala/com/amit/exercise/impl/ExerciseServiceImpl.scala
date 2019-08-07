package com.amit.exercise.impl


import akka.NotUsed
import akka.stream.scaladsl.Source
import com.amit.exercise.{BankInfo, BlackListIp, Category}
import com.amit.exercise.api.ExerciseService
import com.amit.exercise.impl.store.daos.{BankInfoDao, BlackListIpDao, CategoryDao}
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.transport.NotFound
import com.lightbend.lagom.scaladsl.server.ServerServiceCall
import play.api.i18n.{Lang, Langs, MessagesApi}

import scala.concurrent.ExecutionContext
/**
  * Implementation of the BsbecService.
  */
class ExerciseServiceImpl(
                           bankInfoDao: BankInfoDao,
                           blackListIpDao: BlackListIpDao,
                           fileCategoryDao: CategoryDao,
                           messagesApi: MessagesApi,
                           languages: Langs)
(implicit ec: ExecutionContext) extends ExerciseService {



  override def processContract(typeKey: String): ServiceCall[NotUsed, String] = ???

  override def urlDynamicPart: ServiceCall[Seq[String], Seq[String]] = ???

  override def getBankInfoByIdentifier(identifier: String): ServiceCall[NotUsed, BankInfo] =     ServiceCall { _ =>
    bankInfoDao.getByIdentifier(identifier).map {
      case Some(bi) => bi
      case None => throw NotFound(s"No Bank Info found with identifier ${identifier}")
    }recover {
      case ex: Exception => throw ex
    }
  }

  override def createBankInfo: ServiceCall[BankInfo, BankInfo] = ServiceCall { bi =>
    bankInfoDao.create(bi).map(x=>x).recover {
      case ex: Exception => throw ex
    }
  }

  override def deleteBankInfo(identifier: String): ServiceCall[NotUsed, String] =  ServiceCall { bi =>
    bankInfoDao.deleteByIdentifier(identifier).map(x =>
      s"Bank Info with identifier ${identifier} delete ${x} "
      ).recover {
      case ex: Exception => throw ex
    }
  }

  override def createBackListIp: ServiceCall[BlackListIp, BlackListIp] = ???

  override def deleteBackListIp(ip: Long): ServiceCall[NotUsed, BlackListIp] = ???

  override def isBlackListIp(ip: Long): ServiceCall[NotUsed, Boolean] = ???

  override def createCategory: ServiceCall[Category, Category] = ???

  override def deleteCategory(title: String): ServiceCall[NotUsed, Category] = ???

  override def getCategoryByTitle(ip: Long): ServiceCall[NotUsed, Category] = ???
}
