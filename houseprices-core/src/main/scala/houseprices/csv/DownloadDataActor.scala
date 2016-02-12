package houseprices.csv

import akka.actor.Actor

object DownloadDataActor {
  case class Download(url: String)
  case class DownloadResult(url: String, filePath: String)
  case class Failure(msg: String)
}

class DownloadDataActor extends Actor {
  import DownloadDataActor._
  def receive = {
    case Download(url) =>
      sender ! DownloadResult(url, "somepath");
      context.become(downloading)
  }

  def downloading: Receive = {
    case _: Download => sender ! Failure("sorry can only download one file at a time")
  }
}