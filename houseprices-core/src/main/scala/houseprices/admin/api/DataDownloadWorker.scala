package houseprices.admin.api

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.Executor
import scala.annotation.implicitNotFound
import scala.concurrent.ExecutionContext
import scala.util.Failure
import scala.util.Success
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
import akka.stream.ActorMaterializer

class DataDownloadWorker(saveToFolder: String) extends Actor with ActorLogging {

  this: HttpRequestService =>

  import DataDownloadMessages._
  implicit val executor = context.dispatcher.asInstanceOf[Executor with ExecutionContext]

  val oneGigabyte = 1073741824
  val http = Http(context.system)
  implicit val materializer = ActorMaterializer()

  createFolder(saveToFolder)

  def receive = {
    case Download(url, fileName) =>
      downloadFileFrom(url)
      context.become(downloading(sender, url, fileName))
  }

  def downloading(originalSender: ActorRef, url: String, fileName: String): Receive = {
    case HttpResponse(StatusCodes.OK, headers, entity, _) =>
      log.info("got response")
      saveEntityToFile(entity.withSizeLimit(oneGigabyte), originalSender, url, makeFilePath(saveToFolder, fileName))
    case HttpResponse(code, _, _, _) =>
      log.info("request failed, response code: " + code)
    case _: Download => sender ! DownloadFailure("Sorry already downloading")
  }

  def downloadFileFrom(url: String): Unit = {
    log.info("downloading from {}", url)
    http.singleRequest(HttpRequest(uri = url)) pipeTo self
  }

  def saveEntityToFile(entity: ResponseEntity, sender: ActorRef, url: String, filePath: String) = {
    entity.dataBytes.runWith(fileWriter(filePath)) onComplete { t =>
      t match {
        case Success(size) =>
          log.info("completed file, size {}", size)
          sender ! DownloadResult(url, filePath)
        case Failure(f) => println(f)
      }
      context.become(receive)
    }
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

  override def postStop() = {
    http.shutdownAllConnectionPools()
    log.info("shutdown http conn pools")
  }

}