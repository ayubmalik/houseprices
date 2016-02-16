package houseprices.csv

import akka.actor.ActorSystem
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import akka.actor.Props

object DataDownloadApp extends App {

  import Messages._

  val system = ActorSystem("dataDownloader")
  val downloader = system.actorOf(Props(classOf[DataDownloader], "/tmp/houseprices/"))
  downloader ! Download("http://prod1.publicdata.landregistry.gov.uk.s3-website-eu-west-1.amazonaws.com/pp-monthly-update-new-version.csv", "latest.csv")
 // Await.result(system.terminate(), Duration.create(2, "seconds"))
}