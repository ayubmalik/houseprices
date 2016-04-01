package houseprices.search

import org.scalatest.Matchers
import org.scalatest.WordSpec
import akka.actor.ActorSystem
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.model.HttpMethods
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.MediaTypes.`application/json`
import houseprices.search.model.Query
import scala.concurrent.Future
import scala.collection.Searching.SearchResult
import houseprices.search.model.SearchResult
import houseprices.search.model.PricePaidData
import spray.json._

class HousePriceSearchServerSpec extends WordSpec
    with Matchers with ScalatestRouteTest with HousePriceSearchService {

  implicit val ec = system.dispatcher

  val searchClient = new SearchClient {
    def search(qry: Query) = Future { SearchResult(1, List(PricePaidData(5, "today", "address1"))) }
  }

  "HousePriceSearchServer" when {

    "searching" should {

      "return search result" in {
        val getRequest = HttpRequest(HttpMethods.GET, uri = "/search/some+query")
        getRequest ~> routes ~> check {
          status.isSuccess() shouldEqual true
          responseEntity shouldEqual HttpEntity(`application/json`,
            SearchResult(1, List(PricePaidData(5, "today", "address1"))).toJson.prettyPrint)
        }
      }

    }
  }
}
