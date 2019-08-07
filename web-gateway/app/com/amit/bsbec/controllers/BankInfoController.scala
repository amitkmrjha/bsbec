package com.amit.bsbec.controllers

import com.amit.exercise.BankInfo
import com.amit.exercise.api.ExerciseService
import play.api.{Configuration, Logger}
import play.api.i18n.{I18nSupport, Messages}
import play.api.libs.json.{JsError, Json}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import java.io.File
import java.nio.file.{Files, Path}
import akka.stream.{IOResult, Materializer}
import akka.stream.scaladsl._
import akka.util.ByteString

import play.api.libs.json.Json
import play.api.libs.streams._
import play.api.mvc.MultipartFormData.FilePart
import play.core.parsers.Multipart.FileInfo

import scala.language.postfixOps
import scala.concurrent.{ExecutionContext, Future}

class BankInfoController (
                           cc: ControllerComponents,
                           configuration: Configuration,
                           exerciseService: ExerciseService)
                         (implicit ec: ExecutionContext,mat:Materializer)
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

  def deleteBankInfo(identifier:String): Action[AnyContent] = Action.async { implicit request =>
    logger.info(Messages("action.request.info", request.id, request.method, request.uri))
    exerciseService.deleteBankInfo(identifier).invoke.map{ x=>
      Ok(Json.toJson(x))
    }.recover{
      case ex:Exception => InternalServerError(s"${ex.getLocalizedMessage}")
    }
  }


  /**
    * Uploads a multipart file as a POST request.
    *
    * @return
    */
  def uploadInfo = Action.async(parse.multipartFormData(handleFilePartAsFile)) { implicit request =>
    val fileOption = request.body.file("name").map {
      case FilePart(key, filename, contentType, file, fileSize, dispositionType) =>
        logger.info(s"key = $key, filename = $filename, contentType = $contentType, file = $file, fileSize = $fileSize, dispositionType = $dispositionType")
        file
    }
    fileOption match {
      case Some(f) => parseOnTempFile(f).flatMap{str =>
        val data = operateOnTempFile(f)
        val bankInfoSeq = toBankInfo(str)
        exerciseService.createBankInfoSeq.invoke(bankInfoSeq).map{ x=>
          Ok(Json.toJson(x))
        }.recover{
          case ex:Exception => InternalServerError(s"${ex.getLocalizedMessage}")
        }
      }
      case None => Future.successful(Ok(s"No file to process"))
    }
  }


  type FilePartHandler[A] = FileInfo => Accumulator[ByteString, FilePart[A]]

  /**
    * Uses a custom FilePartHandler to return a type of "File" rather than
    * using Play's TemporaryFile class.  Deletion must happen explicitly on
    * completion, rather than TemporaryFile (which uses finalization to
    * delete temporary files).
    *
    * @return
    */
  private def handleFilePartAsFile: FilePartHandler[File] = {
    case FileInfo(partName, filename, contentType, _) =>
      val path: Path = Files.createTempFile("multipartBody", "tempFile")
      val fileSink: Sink[ByteString, Future[IOResult]] = FileIO.toPath(path)
      val accumulator: Accumulator[ByteString, IOResult] = Accumulator(fileSink)
      accumulator.map {
        case IOResult(count, status) =>
          logger.info(s"count = $count, status = $status")
          FilePart(partName, filename, contentType, path.toFile)
      }
  }

  /**
    * A generic operation on the temporary file that deletes the temp file after completion.
    */
  private def parseOnTempFile(file: File):Future[Seq[String]] = {
    FileIO.fromPath(file.toPath)
      .via(Framing.delimiter(ByteString(System.lineSeparator), maximumFrameLength = 512, allowTruncation = true))
      .map(_.utf8String)
      .runWith(Sink.seq[String])
  }

  private def toBankInfo(lines:Seq[String]):Seq[BankInfo] = lines.map{ line =>
    val infoArray = line.split(";")
    BankInfo(None,infoArray(0),infoArray(1))
  }



  /**
    * A generic operation on the temporary file that deletes the temp file after completion.
    */
  private def operateOnTempFile(file: File) = {
    val size = Files.size(file.toPath)
    logger.info(s"size = ${size}")
    Files.deleteIfExists(file.toPath)
    size
  }


}
