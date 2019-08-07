package com.amit.bsbec.controllers

import com.amit.exercise.api.ExerciseService
import play.api.{Configuration, Logger}
import play.api.i18n.{I18nSupport, Messages}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import scala.concurrent.ExecutionContext

class ContractController (
                           cc: ControllerComponents,
                           configuration: Configuration,
                           exerciseService: ExerciseService)
                         (implicit ec: ExecutionContext)
  extends AbstractController(cc)
    with I18nSupport {

  val logger = Logger(this.getClass)

  def processContract(typeKey:String):Action[AnyContent] = Action.async { implicit request =>
    logger.info(Messages("action.request.info", request.id, request.method, request.uri))
    exerciseService.processContract(typeKey).invoke.map { x =>
      Ok(Json.toJson(x))
    }.recover {
      case ex: Exception => InternalServerError(s"${ex.getLocalizedMessage}")
    }
  }
}
