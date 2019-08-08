package com.amit.bsbec.controllers

import java.io.File
import java.nio.file.{Files, Path}

import akka.stream.{IOResult, Materializer}
import akka.stream.scaladsl.{FileIO, Framing, Sink}
import akka.util.ByteString
import com.amit.exercise.{BankInfo, Category}
import com.amit.exercise.api.ExerciseService
import play.api.{Configuration, Logger}
import play.api.i18n.{I18nSupport, Messages}
import play.api.libs.json.{JsError, Json}
import play.api.libs.streams.Accumulator
import play.api.mvc.MultipartFormData.FilePart
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import play.core.parsers.Multipart.FileInfo

import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source

class CategoryController (
                           cc: ControllerComponents,
                           configuration: Configuration,
                           exerciseService: ExerciseService)
                         (implicit ec: ExecutionContext,mat:Materializer)
  extends AbstractController(cc)
    with I18nSupport {

  val logger = Logger(this.getClass)

  def getByTitle(title:String): Action[AnyContent] = Action.async { implicit request =>
    logger.info(Messages("action.request.info", request.id, request.method, request.uri))
    exerciseService.getCategoryByTitle(title).invoke.map{ x=>
      Ok(Json.toJson(x))
    }.recover{
      case ex:Exception => InternalServerError(s"${ex.getLocalizedMessage}")
    }
  }

  def createCategory = Action.async(parse.json) { implicit request =>
    logger.info(Messages("action.request.info", request.id, request.method, request.uri))
    request.body.validate[Category].fold(
      { errors =>    Future.successful(BadRequest(Json.toJson(JsError.toJson(errors).toString())))
      }, {
        category =>
          exerciseService.createCategory.invoke(category).map{ x=>
            Ok(Json.toJson(x))
          }.recover{
            case ex:Exception => InternalServerError(s"${ex.getLocalizedMessage}")
          }
      })
  }

  def deleteCategory(title:String): Action[AnyContent] = Action.async { implicit request =>
    logger.info(Messages("action.request.info", request.id, request.method, request.uri))
    exerciseService.deleteCategory(title).invoke.map{ x=>
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
  def getFileCategory = Action.async(parse.multipartFormData(handleFilePartAsFile)) { implicit request =>
    val fileOption = request.body.file("name").map {
      case FilePart(key, filename, contentType, file, fileSize, dispositionType) =>
        logger.info(s"key = $key, filename = $filename, contentType = $contentType, file = $file, fileSize = $fileSize, dispositionType = $dispositionType")
        file
    }
    fileOption match {
      case Some(f) => parseOnTempFile(f).flatMap{str =>
        val data = operateOnTempFile(f)
        fileResult(str)
        Future.successful(Ok(Json.toJson(fileResult(str).map(m => m._1._1 -> m._1._2))))
      }
      case None => Future.successful(Ok(s"No file to process"))
    }
  }

  private def fileResult(lines:Seq[String]):Seq[((String, Int), Int)] = {
    lines.filter(_.nonEmpty)
      .flatMap(_.split("""\W+"""))
      .groupBy(_.toLowerCase())
      .mapValues(_.size).toSeq
      .sortWith { case ((_, v0), (_, v1)) => v0 > v1 }
      .zipWithIndex
  }

  lazy val keyWordDictionary:Future[Map[String,String]] = {
    exerciseService.getKeyWordTitle.invoke(Seq.empty[String]).map{ x =>
      x.map(kw => kw.keyword->kw.title).toMap
    }.recover{
      case ex:Exception => Map.empty[String,String]
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
