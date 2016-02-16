package houseprices.csv

import akka.actor.ActorSystem
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object DataDownloadApp extends App {

  val system = ActorSystem("dataDownloader")
  Await.result(system.terminate(), Duration.create(2, "seconds"))
}