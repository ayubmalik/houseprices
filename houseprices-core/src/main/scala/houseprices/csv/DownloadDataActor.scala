package houseprices.csv

import java.util.concurrent.Executor
import scala.concurrent.ExecutionContext
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes
import akka.pattern.pipe
import akka.stream.scaladsl.ImplicitMaterializer
import akka.stream.scaladsl.Source
import akka.util.ByteString
import akka.stream.scaladsl.Sink
import akka.stream.scaladsl.FileIO
import java.io.File
import akka.actor.ActorRef

object DownloadDataActor {
  case class Download(url: String)
  case class DownloadResult(url: String, filePath: String)
  case class Failure(msg: String)
}

class DownloadDataActor extends Actor with ImplicitMaterializer with ActorLogging {
  implicit val executor = context.dispatcher.asInstanceOf[Executor with ExecutionContext]
  import DownloadDataActor._
  val http = Http(context.system)

  def receive = {
    case Download(url) =>
      downloadFileFrom(url)
      context.become(downloading(sender, url))
  }

  def downloading(originalSender: ActorRef, url: String): Receive = {
    case _: Download => sender ! Failure("sorry can only download one file at a time")
    case HttpResponse(StatusCodes.OK, headers, entity, _) =>
      log.info("Got response, body: " + entity.toString())
      entity.dataBytes.runWith(FileIO.toFile(new File("/tmp/secret.txt")))
      originalSender ! DownloadResult(url, "/tmp/secret.txt")
    case HttpResponse(code, _, _, _) =>
      log.info("Request failed, response code: " + code)
  }

  def downloadFileFrom(url: String): Unit = {
    log.info("downloading from {}", url)
    http.singleRequest(HttpRequest(uri = url)) pipeTo self
  }
}