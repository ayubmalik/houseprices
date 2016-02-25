package houseprices.admin.dataimport

import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import org.scalatest.BeforeAndAfterEach
import org.scalatest.Matchers
import org.scalatest.WordSpecLike
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.http.scaladsl.model.HttpMethod
import akka.pattern.ask
import akka.testkit.ImplicitSender
import akka.testkit.TestActorRef
import akka.testkit.TestKit
import akka.util.Timeout

class DataImporterSpec extends TestKit(ActorSystem("dataimporter"))
    with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterEach {

  implicit val ec = system.dispatcher

  "DataImporter" when {

    "running" should {

      "import data from csv file" in {
        val importer = TestActorRef(new DataImporter("/tmp/houseprices"))
        importer ! ImportData("some file")
        expectMsg("TODO")
        system.stop(importer)
      }

    }
  }
}