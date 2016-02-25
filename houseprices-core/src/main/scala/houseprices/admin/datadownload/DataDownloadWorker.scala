package houseprices.admin.datadownload

import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.Executor

import scala.concurrent.ExecutionContext
import scala.util.Failure
import scala.util.Success

import DataDownloadMessages.DownloadFailure
import DataDownloadMessages.DownloadResult
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorRef
import akka.actor.actorRef2Scala

class DataDownloadWorker(client: HttpClient, saveToFolder: String) extends Actor with ActorLogging {

  import DataDownloadMessages._
  implicit val executor = context.dispatcher.asInstanceOf[Executor with ExecutionContext]

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

}