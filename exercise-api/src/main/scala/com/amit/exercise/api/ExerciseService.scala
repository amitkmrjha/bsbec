package com.amit.exercise.api

import akka.NotUsed
import akka.stream.scaladsl.Source
import com.amit.exercise.{BankInfo, BlackListIp, Category}
import com.amit.exercise
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}

/**
  * The bsbec stream interface.
  *
  * This describes everything that Lagom needs to know about how to serve and
  * consume the BsbecStream service.
  */
trait ExerciseService extends Service {

  def processContract(typeKey:String): ServiceCall[NotUsed, String]
  def urlDynamicPart: ServiceCall[Seq[String], Seq[String]]


  def createBankInfo:ServiceCall[BankInfo, BankInfo]
  def deleteBankInfo(identifier:String): ServiceCall[NotUsed, String]
  def getBankInfoByIdentifier(identifier:String): ServiceCall[NotUsed, BankInfo]

  def createBackListIp:ServiceCall[BlackListIp, BlackListIp]
  def deleteBackListIp(ip:Long): ServiceCall[NotUsed, String]
  def isBlackListIp(ip:Long): ServiceCall[NotUsed, Boolean]

  def createCategory:ServiceCall[Category, Category]
  def deleteCategory(title:String): ServiceCall[NotUsed, String]
  def getCategoryByTitle(title:String): ServiceCall[NotUsed, Category]


  //

  override final def descriptor: Descriptor = {
    import Service._

    named("exercise")
      .withCalls(
        restCall(Method.GET, "/api/v1/contract/process/:typeKey", processContract _),
        restCall(Method.POST, "/api/v1/url/dynamic/part", urlDynamicPart _),

        restCall(Method.POST, "/api/v1/bank/info", createBankInfo _),
        restCall(Method.DELETE, "/api/v1/bank/info/:identifier", deleteBankInfo _),
        restCall(Method.GET, "/api/v1/bank/info/:identifier", getBankInfoByIdentifier _),

        restCall(Method.POST, "/api/v1/backlist/ip", createBackListIp _),
        restCall(Method.DELETE, "/api/v1/backlist/ip/:ip", deleteBackListIp _),
        restCall(Method.GET, "/api/v1/backlist/ip/:ip", isBlackListIp _),

        restCall(Method.POST, "/api/v1/category", createCategory _),
        restCall(Method.DELETE, "/api/v1/category/:title", deleteCategory _),
        restCall(Method.GET, "/api/v1/category/:title", getCategoryByTitle _)
      ).withAutoAcl(true)
  }
}

