package com.intercax.syndeia.loader

import java.time.Clock

import com.amit.bsbec.controllers.{BankInfoController, CategoryController, ContractController, HomeController}
import com.amit.bsbec.filters.{AllowedIpFilter, CustomHttpFilterComponents, ExampleFilter}
import com.amit.exercise.api.ExerciseService
import com.intercax.syndeia.filters.LoggingFilter
import com.lightbend.lagom.scaladsl.api.{LagomConfigComponent, ServiceAcl, ServiceInfo}
import com.lightbend.lagom.scaladsl.client.{ConfigurationServiceLocatorComponents, LagomServiceClientComponents}
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.softwaremill.macwire._
import play.api.ApplicationLoader.Context
import play.api.libs.ws.ahc.AhcWSComponents
import play.api.{ApplicationLoader, BuiltInComponentsFromContext, LoggerConfigurator, Mode}
import play.filters.HttpFiltersComponents
import play.filters.cors.CORSComponents
import play.filters.csrf.CSRFComponents
import play.filters.gzip.GzipFilterComponents
import play.filters.headers.SecurityHeadersComponents
import play.filters.hosts.AllowedHostsComponents

import scala.collection.immutable
import scala.concurrent.ExecutionContext
import router.Routes

abstract class WebGateway(context: Context) extends BuiltInComponentsFromContext(context)
  /*with HttpFiltersComponents*/
  with CustomHttpFilterComponents
  with GzipFilterComponents
  with  CSRFComponents
  with CORSComponents
  with SecurityHeadersComponents
  with AllowedHostsComponents
  with AhcWSComponents
  with LagomConfigComponent
  with LagomServiceClientComponents
  with controllers.AssetsComponents{

  override lazy val serviceInfo: ServiceInfo = ServiceInfo(
    "web-gateway",
    Map(
      "web-gateway" -> immutable.Seq(ServiceAcl.forPathRegex("(?!/api/).*"))
    )
  )
  override implicit lazy val executionContext: ExecutionContext = actorSystem.dispatcher


   lazy val router  = {
    val prefix = "/"
    wire[Routes]
  }

  lazy val bankInfoController: BankInfoController = wire[BankInfoController]
  lazy val categoryController: CategoryController = wire[CategoryController]
  lazy val contractController: ContractController = wire[ContractController]
  lazy val homeController: HomeController = wire[HomeController]

  lazy val exerciseService = serviceClient.implement[ExerciseService]

  lazy val securedBodyParser: play.api.mvc.BodyParsers.Default = wire[play.api.mvc.BodyParsers.Default]

  lazy val unsecuredBodyParser: play.api.mvc.BodyParsers.Default = wire[play.api.mvc.BodyParsers.Default]

  lazy val userAwareBodyParser: play.api.mvc.BodyParsers.Default = wire[play.api.mvc.BodyParsers.Default]

  lazy val javaclock : Clock = Clock.systemDefaultZone

  lazy  val allowedIpFilter: AllowedIpFilter = wire[AllowedIpFilter]
}

class WebGatewayLoader extends ApplicationLoader {

  override def load(context: Context) = {
    LoggerConfigurator(context.environment.classLoader).foreach {
      _.configure(context.environment, context.initialConfiguration, Map.empty)
    }
    context.environment.mode match {
    case Mode.Dev =>
      (new WebGateway(context) with LagomDevModeComponents).application
    case _ =>
      (new WebGateway(context)  with ConfigurationServiceLocatorComponents).application
    }
  }
}