package houseprices.csv

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

class DataDownloader(saveToFolder: String) extends Actor  with ActorLogging {
  implicit val executor = context.dispatcher.asInstanceOf[Executor with ExecutionContext]

  var router = {
    val routees = Vector.fill(2) {
      val r = context.actorOf(Props(classOf[DataDownloadWorker], saveToFolder))
      context watch r
      ActorRefRoutee(r)
    }
    Router(RoundRobinRoutingLogic(), routees)
  }

  def receive = {
    case d: Messages.Download =>
      router.route(d, sender())
    case Terminated(a) =>
      log.warning("{} is terminated", a)
      router = router.removeRoutee(a)
      val r = context.actorOf(Props[DataDownloadWorker])
      context watch r
      router = router.addRoutee(r)
  }

}