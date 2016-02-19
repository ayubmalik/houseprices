package houseprices.csv

import org.scalatest.BeforeAndAfterAll
import org.scalatest.Matchers
import org.scalatest.WordSpecLike

import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.testkit.ImplicitSender
import akka.testkit.TestActorRef
import akka.testkit.TestKit

class DataDownloaderSpec extends TestKit(ActorSystem("test"))
    with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterAll {

  import DataDownloadMessages._

  "DataDownloader" when {

    "starting" should {
      "create workers" in {
        val actorRef = TestActorRef(new DataDownloader("/tmp/1"))
        actorRef.underlyingActor.router.routees.size should be(2)
      }
    }

    "running" should {
      "should limit downloads" in {
        val downloader = system.actorOf(Props(classOf[DataDownloader], "/tmp/1"))
        downloader ! Download("http://made.up.url/path", "file1")
        downloader ! Download("http://made.up.url/path", "file2")
        downloader ! Download("http://made.up.url/path", "file3")
        expectMsg(DownloadFailure("Sorry already downloading"))
      }
    }
  }
}