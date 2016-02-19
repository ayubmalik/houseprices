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
import java.nio.charset.Charset

class DataDownloadWorker(client: HttpClient, saveToFolder: String) extends Actor with ActorLogging {

  import DataDownloadMessages._
  implicit val executor = context.dispatcher.asInstanceOf[Executor with ExecutionContext]

  val oneGigabyte = 1073741824
  val http = Http(context.system)
  implicit val materializer = ActorMaterializer()

  createFolder(saveToFolder)

  def receive = {
    case Download(url, fileName) =>
      downloadFile(sender, url, makeFilePath(saveToFolder, fileName))
      context.become(downloading)
  }

  def downloading: Receive = {
    case _: Download => sender ! DownloadFailure("Sorry already downloading")
  }

  def downloadFile(originalSender: ActorRef, url: String, filePath: String): Unit = {
    log.info("downloading from {}", url)
    client.get(url).map { body => saveToFile(filePath, body) } onComplete {
      case Success(size) => originalSender ! DownloadResult(url, filePath)
      case Failure(t) => println("An error has occured: " + t.getMessage)
    }
  }

  def saveToFile(filePath: String, body: String) = {
    println("*** saving ****", body, filePath)
    context.become(receive)
    Files.write(Paths.get(filePath), body.getBytes)
    body.length()
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