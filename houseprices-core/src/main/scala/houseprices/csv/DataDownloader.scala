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
import akka.actor.Props
import akka.routing.ActorRefRoutee
import akka.routing.Router
import akka.routing.RoundRobinRoutingLogic
import akka.actor.Terminated

class DataDownloader extends Actor with ImplicitMaterializer with ActorLogging {
  implicit val executor = context.dispatcher.asInstanceOf[Executor with ExecutionContext]

  var router = {
    val routees = Vector.fill(2) {
      val r = context.actorOf(Props(classOf[DownloadWorker], "/tmp/downloads"))
      context watch r
      ActorRefRoutee(r)
    }
    Router(RoundRobinRoutingLogic(), routees)
  }

  def receive = {
    case d: Messages.Download =>
      router.route(d, sender())
    case Terminated(a) =>
      log.info("{} is terminated", a)
      router = router.removeRoutee(a)
      val r = context.actorOf(Props[DownloadWorker])
      context watch r
      router = router.addRoutee(r)
  }

}