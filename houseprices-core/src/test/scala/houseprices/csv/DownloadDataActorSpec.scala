package houseprices.csv

import org.scalatest.BeforeAndAfterAll
import org.scalatest.Matchers
import org.scalatest.WordSpecLike
import akka.actor.ActorSystem
import akka.actor.actorRef2Scala
import akka.testkit.ImplicitSender
import akka.testkit.TestKit
import akka.actor.Props

class DownloadDataActorSpec(_system: ActorSystem) extends TestKit(_system) with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterAll {

  import DownloadDataActor._

  def this() = this(ActorSystem("TestSystem"))

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "DownloadDataActor" when {

    "downloading data" should {

      "make HTTP request" in {

        val downloadActor = system.actorOf(Props[DownloadDataActor]);
        downloadActor ! Download("http://textfiles.com/computers/secret.txt");
        expectMsg(DownloadResult("http://textfiles.com/computers/secret.txt", "somepath"));
      }
    }
  }

}