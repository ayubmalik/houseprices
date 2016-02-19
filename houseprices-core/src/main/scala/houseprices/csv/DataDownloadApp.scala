package houseprices.csv

import akka.actor.ActorSystem
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import akka.actor.Props
import akka.actor.Inbox
import akka.pattern.gracefulStop
import scala.concurrent.ExecutionContext.Implicits.global

object DataDownloadApp extends App {

  import DataDownloadMessages._

  val file1 = "http://prod1.publicdata.landregistry.gov.uk.s3-website-eu-west-1.amazonaws.com/pp-2015.csv"
  val file2 = "http://prod1.publicdata.landregistry.gov.uk.s3-website-eu-west-1.amazonaws.com/pp-monthly-update-new-version.csv"

  val system = ActorSystem("dataDownloader")
  val inbox = Inbox.create(system)
  val downloader = system.actorOf(Props(classOf[DataDownloader], "/tmp/houseprices"))

  inbox.send(downloader, Download(file2, "pp-data-file.csv"))
  val msg = inbox.receive(2.minutes)

  gracefulStop(downloader, 1.seconds) onComplete { _ =>
    system.terminate()
    println("Done. Message was: " + msg)
  }
}