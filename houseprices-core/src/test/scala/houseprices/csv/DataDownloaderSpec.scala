package houseprices.csv

import akka.testkit.TestKit
import akka.testkit.ImplicitSender
import org.scalatest.BeforeAndAfterAll
import akka.actor.ActorSystem
import org.scalatest.Matchers
import org.scalatest.WordSpecLike
import akka.actor.Props
import akka.testkit.TestActorRef

class DataDownloaderSpec extends TestKit(ActorSystem("test"))
    with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterAll {

  "DataDownloader" when {

    "starting" should {

      "create 2 workers" in {
        val actorRef = TestActorRef[DataDownloader]
        actorRef.underlyingActor.router.routees.size should be(2)
      }
    }
  }
}