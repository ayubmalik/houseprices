package houseprices.search.model

import java.time.LocalDate.parse
import scala.io.Source
import org.scalatest.Finders
import org.scalatest.Matchers
import org.scalatest.WordSpec
import spray.json.JsonParser
import spray.json.ParserInput.apply
import spray.json.JsObject
import akka.http.scaladsl.unmarshalling.Unmarshaller
import akka.http.scaladsl.model.HttpEntity

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
      pricepaid should be(PricePaidData(274950, "2009-07-10", "11, LOUGHBOROUGH DRIVE, BROUGHTON, MILTON KEYNES, MILTON KEYNES, MK10 7DZ"))
    }

  }

}