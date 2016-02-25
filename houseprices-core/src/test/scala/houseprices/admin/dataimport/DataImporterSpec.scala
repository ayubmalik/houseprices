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
import org.elasticsearch.client.Client
import org.mockito.Mockito._
import org.mockito.Matchers.anyObject
import houseprices.elasticsearch.BulkAddPricePaid

class DataImporterSpec extends TestKit(ActorSystem("dataimporter"))
    with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterEach {

  implicit val ec = system.dispatcher

  "DataImporter" when {

    "running" should {

      "import data from csv file" in {
        var fullPathToDownload = ""
        val bulkAddFactory = (csvFile: String) => new BulkAddPricePaid(null, csvFile) {
          override def run = { fullPathToDownload = csvFile }
        }

        val importer = TestActorRef(new DataImporter("/tmp/houseprices", bulkAddFactory))

        importer ! ImportData("somefile")
        fullPathToDownload shouldBe "/tmp/houseprices/somefile"
        system.stop(importer)

      }
    }
  }
}