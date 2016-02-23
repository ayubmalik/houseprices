package houseprices.admin.api

import java.util.concurrent.Executor
import scala.annotation.implicitNotFound
import scala.concurrent.ExecutionContext
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.Props
import akka.actor.Terminated
import akka.routing.ActorRefRoutee
import akka.routing.RoundRobinRoutingLogic
import akka.routing.Router
import akka.actor.ActorRef

class DataDownloadManager(saveToFolder: String, client: HttpClient) extends Actor with ActorLogging {
  import DataDownloadMessages._
  implicit val executor = context.dispatcher.asInstanceOf[Executor with ExecutionContext]

  var active = Map.empty[String, (Download, ActorRef)]

  def receive = {
    case download: Download =>
      addDownload(download, sender)
    case ShowActive => sender ! ActiveWorkers(active.keys.size)
    case DownloadResult(url, filepath) =>
      println("todo")

  }

  def addDownload(download: Download, orig: ActorRef) = {
    if (active.size == 2) orig ! DownloadFailure("Sorry max limit reached")
    else {
      active += (download.url -> (download, sender))
      val worker = context.actorOf(Props(classOf[DataDownloadWorker], client, saveToFolder))
      worker ! download
    }
  }
}