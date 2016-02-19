package houseprices.csv

import scala.concurrent.duration.DurationInt
import org.scalatest.BeforeAndAfterAll
import org.scalatest.Matchers
import org.scalatest.WordSpecLike
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.testkit.ImplicitSender
import akka.testkit.TestKit
import scala.util.Random
import java.nio.file.Paths
import java.nio.file.Files
import akka.actor.PoisonPill

class DataDownloadWorkerSpec(_system: ActorSystem) extends TestKit(_system) with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterAll {

  import DataDownloadMessages._

  def this() = this(ActorSystem("TestSystem"))

  override def afterAll {
    TestKit.shutdownActorSystem(system, 1.seconds, true)
  }

  "DownloadWorker" when {

    "downloading data" should {

      "save file" in {
        val worker = system.actorOf(Props(classOf[DataDownloadWorker], "/tmp/downloads"))
        worker ! Download("http://textfiles.com/computers/secret.txt", "secret.txt")
        expectMsg(3.seconds, DownloadResult("http://textfiles.com/computers/secret.txt", "/tmp/downloads/secret.txt"))
      }
    }

    "starting" should {

      "create output folder" in {
        val saveToFolder = "/tmp/tst_" + Random.alphanumeric.take(5).mkString
        Files.isDirectory(Paths.get(saveToFolder)) should be(false)

        system.actorOf(Props(classOf[DataDownloadWorker], saveToFolder))
        within(500.millis) {
          Files.isDirectory(Paths.get(saveToFolder)) should be(true)
          Files.deleteIfExists(Paths.get(saveToFolder))
        }
      }
    }
  }

}