package houseprices.csv

import akka.testkit.TestKit
import akka.testkit.ImplicitSender
import org.scalatest.BeforeAndAfterAll
import akka.actor.ActorSystem
import org.scalatest.Matchers
import org.scalatest.WordSpecLike
import akka.actor.Props
import akka.testkit.TestActorRef
import akka.actor.PoisonPill
import akka.testkit.TestProbe
import scala.actors.remote.Terminate

class DataDownloaderSpec extends TestKit(ActorSystem("test"))
    with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterAll {

  "DataDownloader" when {

    "starting" should {
      "create workers" in {
        val actorRef = TestActorRef[DataDownloader]
        actorRef.underlyingActor.router.routees.size should be(2)
      }
    }

    "running" should {
      "should limit downloads" in {
        val downloader = system.actorOf(Props[DataDownloader])
        downloader ! Messages.Download("http://made.up.url/path", "file1")
        downloader ! Messages.Download("http://made.up.url/path", "file2")
        downloader ! Messages.Download("http://made.up.url/path", "file3")
        expectMsg(Messages.Failure("Sorry already downloading"))
      }
    }
  }
}