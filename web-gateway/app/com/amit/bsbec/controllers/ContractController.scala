package com.amit.bsbec.controllers

import com.amit.exercise.api.ExerciseService
import play.api.Configuration
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext

class ContractController (
                           cc: ControllerComponents,
                           configuration: Configuration,
                           exerciseService: ExerciseService)
                         (implicit ec: ExecutionContext)
  extends AbstractController(cc)
    with I18nSupport {

}
