package com.amit.exercise.impl


import akka.NotUsed
import akka.stream.scaladsl.Source
import com.amit.exercise._
import com.amit.exercise.api.ExerciseService
import com.amit.exercise.impl.store.daos.{BankInfoDao, BlackListIpDao, CategoryDao, KeyWordTitleDao}
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.transport.NotFound
import com.lightbend.lagom.scaladsl.server.ServerServiceCall
import play.api.i18n.{Lang, Langs, MessagesApi}

import scala.concurrent.{ExecutionContext, Future}
/**
  * Implementation of the BsbecService.
  */
class ExerciseServiceImpl(
                           bankInfoDao: BankInfoDao,
                           blackListIpDao: BlackListIpDao,
                           categoryDao: CategoryDao,
                           keyWordTitleDao:KeyWordTitleDao,
                           messagesApi: MessagesApi,
                           languages: Langs)
(implicit ec: ExecutionContext) extends ExerciseService {



  override def processContract(typeKey: String): ServiceCall[NotUsed, String] = ServiceCall { _ =>
    Future.successful(Contract.processContract(typeKey))
  }

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

  override def createBankInfoSeq: ServiceCall[Seq[BankInfo], Seq[BankInfo]] = ServiceCall { biSeq =>
    bankInfoDao.createBulk(biSeq).recover {
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

  override def createBackListIp: ServiceCall[BlackListIp, BlackListIp] = ServiceCall { bli =>
    blackListIpDao.create(bli).map(x=>x).recover {
      case ex: Exception => throw ex
    }
  }

  override def deleteBackListIp(ip: Long): ServiceCall[NotUsed, String] = ServiceCall { _ =>
    blackListIpDao.delete(ip).map(x =>
    s"Black list ip ${ip} delete ${x} "
  ).recover {
    case ex: Exception => throw ex
  }
  }

  override def isBlackListIp(ip: Long): ServiceCall[NotUsed, Boolean] = ServiceCall { _ =>
    blackListIpDao.isBlackList(ip).recover {
      case ex: Exception => throw ex
    }
  }

  override def createCategory: ServiceCall[Category, Category] = ServiceCall { c =>
    categoryDao.create(c).map(x=>x).recover {
      case ex: Exception => throw ex
    }
  }

  override def getKeyWordTitle: ServiceCall[Seq[String], Seq[KeyWordTitle]] = ServiceCall { keywords =>
    keyWordTitleDao.getKeyWordTitle(keywords).recover {
      case ex: Exception => throw ex
    }
  }

  override def deleteCategory(title: String): ServiceCall[NotUsed, String] = ServiceCall { _ =>
    categoryDao.delete(title).map(x =>
      s"category with title ${title} delete ${x} "
    ).recover {
      case ex: Exception => throw ex
    }
  }

  override def getCategoryByTitle(title: String): ServiceCall[NotUsed, Category] = ServiceCall { _ =>
    categoryDao.getByTitle(title).map {
      case Some(bi) => bi
      case None => throw NotFound(s"No Category found with title ${title}")
    }recover {
      case ex: Exception => throw ex
    }
  }


}
