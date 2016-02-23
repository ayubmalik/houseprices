package houseprices.admin.api

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import org.scalatest.BeforeAndAfterAll
import org.scalatest.Matchers
import org.scalatest.WordSpecLike
import DataDownloadMessages.Download
import DataDownloadMessages.DownloadFailure
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.pattern.ask
import akka.testkit.ImplicitSender
import akka.testkit.TestActorRef
import akka.testkit.TestKit
import akka.util.Timeout
import akka.http.scaladsl.model.HttpMethod
import scala.concurrent.Future

class DataDownloadManagerSpec extends TestKit(ActorSystem("test"))
    with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterAll {

  import DataDownloadMessages._
  implicit val ec = system.dispatcher

  val client = new HttpClient with HttpRequestService {
    def makeRequest(method: HttpMethod, uri: String) = Future { "Hello There" }
  }

  "DataDownloadManager" when {

    "running" should {
      "should limit downloads" in {
        val downloader = system.actorOf(Props(classOf[DataDownloadManager], "/tmp/1", client))
        downloader ! Download("http://url1", "file1")
        downloader ! Download("http://url2", "file2")
        downloader ! Download("http://url3", "file3")
        expectMsg(DownloadFailure("Sorry max limit reached"))
      }

      "should show active workers" in {
        import akka.pattern.ask
        import scala.concurrent.duration._
        implicit val timeout = Timeout(1 seconds)

        val downloader = system.actorOf(Props(classOf[DataDownloader], "/tmp/1", client))
        var active = Await.result(ask(downloader, ShowActive), 500 millis)
        active should equal(ActiveWorkers(2))

      }
    }
  }
}