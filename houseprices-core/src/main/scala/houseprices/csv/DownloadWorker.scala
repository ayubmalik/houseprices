package houseprices.csv

import java.io.File
import java.util.concurrent.Executor
import scala.concurrent.ExecutionContext
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorRef
import akka.actor.actorRef2Scala
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.ResponseEntity
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.Uri.apply
import akka.pattern.pipe
import akka.stream.scaladsl.FileIO
import akka.stream.scaladsl.ImplicitMaterializer
import java.nio.file.Files
import java.nio.file.Paths

class DownloadWorker(saveToFolder: String) extends Actor with ImplicitMaterializer with ActorLogging {
  implicit val executor = context.dispatcher.asInstanceOf[Executor with ExecutionContext]
  import Messages._

  val http = Http(context.system)

  createFolder(saveToFolder)

  def receive = {
    case Download(url, fileName) =>
      downloadFileFrom(url)
      context.become(downloading(sender, url, fileName))
  }

  def downloading(originalSender: ActorRef, url: String, fileName: String): Receive = {
    case HttpResponse(StatusCodes.OK, headers, entity, _) =>
      log.info("Got response")
      saveEntityToFile(entity, makeFilePath(saveToFolder, fileName))
      originalSender ! DownloadResult(url, makeFilePath(saveToFolder, fileName))
    case HttpResponse(code, _, _, _) =>
      log.info("Request failed, response code: " + code)
    case _: Download => sender ! Failure("Sorry already downloading")
  }

  def downloadFileFrom(url: String): Unit = {
    log.info("downloading from {}", url)
    http.singleRequest(HttpRequest(uri = url)) pipeTo self
  }

  def saveEntityToFile(entity: ResponseEntity, filePath: String) = {
    entity.dataBytes.runWith(fileWriter(filePath))
  }

  def fileWriter(fileName: String) = {
    FileIO.toFile(new File(fileName))
  }

  def makeFilePath(saveToFolder: String, fileName: String) = {
    saveToFolder + "/" + fileName
  }

  def createFolder(folder: String) {
    Files.createDirectories(Paths.get(folder))
  }

}