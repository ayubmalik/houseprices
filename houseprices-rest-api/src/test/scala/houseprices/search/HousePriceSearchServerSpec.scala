package houseprices.search

import org.scalatest.Matchers
import org.scalatest.WordSpec
import akka.actor.ActorSystem
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.model.HttpMethods
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.HttpEntity

class HousePriceSearchServerSpec extends WordSpec
    with Matchers with ScalatestRouteTest with HousePriceSearchService {

  implicit val ec = system.dispatcher

  val searchClient = null

  "HousePriceSearchServer" when {

    "searching" should {

      "return search result" in {
        val getRequest = HttpRequest(HttpMethods.GET, uri = "/search/meh")
        getRequest ~> routes ~> check {
          status.isSuccess() shouldEqual true
          //responseEntity shouldEqual HttpEntity(`application/json`, active.toJson.prettyPrint)
        }

      }

    }
  }
}
