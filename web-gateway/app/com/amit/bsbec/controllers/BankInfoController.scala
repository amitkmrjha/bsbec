package com.amit.bsbec.controllers

import com.amit.exercise.BankInfo
import com.amit.exercise.api.ExerciseService
import play.api.{Configuration, Logger}
import play.api.i18n.{I18nSupport, Messages}
import play.api.libs.json.{JsError, Json}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import scala.concurrent.{ExecutionContext, Future}

class BankInfoController (
                           cc: ControllerComponents,
                           configuration: Configuration,
                           exerciseService: ExerciseService)
                         (implicit ec: ExecutionContext)
  extends AbstractController(cc)
    with I18nSupport {

  val logger = Logger(this.getClass)

  def getByIdentifier(identifier:String): Action[AnyContent] = Action.async { implicit request =>
   logger.info(Messages("action.request.info", request.id, request.method, request.uri))
   exerciseService.getBankInfoByIdentifier(identifier).invoke.map{ x=>
     Ok(Json.toJson(x))
   }.recover{
     case ex:Exception => InternalServerError(s"${ex.getLocalizedMessage}")
   }
  }

  def createBankInfo = Action.async(parse.json) { implicit request =>
    logger.info(Messages("action.request.info", request.id, request.method, request.uri))
    request.body.validate[BankInfo].fold(
      { errors =>    Future.successful(BadRequest(Json.toJson(JsError.toJson(errors).toString())))
      }, {
        bankInfo =>
          exerciseService.createBankInfo.invoke(bankInfo).map{ x=>
            Ok(Json.toJson(x))
          }.recover{
            case ex:Exception => InternalServerError(s"${ex.getLocalizedMessage}")
          }
      })
  }

}
