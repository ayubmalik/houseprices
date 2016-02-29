package houseprices.search.model

import org.scalatest.WordSpec
import org.scalatest.Matchers
import scala.io.Source
import spray.json.JsValue
import spray.json.JsonParser
import java.time.LocalDate.parse

class SearchResultJsonProtocolSpec extends WordSpec with Matchers {

  import SearchResultJsonProtocol._

  val src = Source.fromFile("src/test/resources/sampledata/es_pricepaid_results_3_prices.json").mkString

  "SearchResultJsonProtocol" should {

    val searchResult = JsonParser(src).convertTo[SearchResult]

    "convert hits to count" in {
      searchResult.count should be(3)
    }

    "create price data as list" in {
      searchResult.priceData.size should be(3)
      val pricepaid = searchResult.priceData(0)
      pricepaid should be(PricePaidData(274950, parse("2009-07-10")))
    }
  }

}