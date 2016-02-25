package houseprices.admin.datadownload

import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

import org.scalatest.BeforeAndAfterEach
import org.scalatest.Matchers
import org.scalatest.WordSpecLike

import DataDownloadMessages.ActiveDownload
import DataDownloadMessages.Download
import DataDownloadMessages.DownloadFailure
import DataDownloadMessages.DownloadResult
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.http.scaladsl.model.HttpMethod
import akka.pattern.ask
import akka.testkit.ImplicitSender
import akka.testkit.TestActorRef
import akka.testkit.TestKit
import akka.util.Timeout

class DataDownloadManagerSpec extends TestKit(ActorSystem("datadownloadmanager"))
    with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterEach {

  import DataDownloadMessages._
  implicit val ec = system.dispatcher

  val client = new HttpClient with HttpRequestService {
    def makeRequest(method: HttpMethod, uri: String) = Future {
      "Hello There"
    }
  }

  "DataDownloadManager" when {

    "running" should {
      "should limit downloads" in {
        val downloader = TestActorRef(new DataDownloadManager("/tmp/houseprices", client))
        downloader.underlyingActor.activeDownloads += ("url1" -> (ActiveDownload("url1", "file1", "now"), self))
        downloader.underlyingActor.activeDownloads += ("url2" -> (ActiveDownload("url2", "file1", "now"), self))
        downloader ! Download("url3", "file3")
        expectMsg(DownloadFailure("Sorry max limit reached"))
        system.stop(downloader)
      }

      "should show active workers" in {
        import akka.pattern.ask
        import scala.concurrent.duration._
        implicit val timeout = Timeout(1 seconds)

        val downloader = TestActorRef(new DataDownloadManager("/tmp/houseprices", client))
        downloader.underlyingActor.activeDownloads += ("url1" -> (ActiveDownload("url1", "file", "now"), self))
        val active: ActiveDownloads = Await.result(ask(downloader, ShowActive), 50 millis).asInstanceOf[ActiveDownloads]
        active.count should be(1)
      }

      "should send DownloadResult to sender" in {
        val downloader = system.actorOf(Props(classOf[DataDownloadManager], "/tmp/houseprices", client))
        downloader ! Download("someurl", "file1")
        expectMsg(DownloadResult("someurl", "/tmp/houseprices/file1"))
        system.stop(downloader)
      }
    }
  }
}