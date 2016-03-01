package houseprices.search.model
import spray.json.DefaultJsonProtocol
import spray.json.JsArray
import spray.json.RootJsonFormat
import spray.json.JsString
import spray.json.JsNumber
import spray.json._
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import akka.http.scaladsl.unmarshalling.Unmarshaller
import akka.http.scaladsl.model.HttpEntity

object SearchResultJsonProtocol extends DefaultJsonProtocol {

  private val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

  implicit object SearchResultJsonFormat extends RootJsonFormat[SearchResult] {

    def write(s: SearchResult) = throw new RuntimeException("TODO")

    def read(value: JsValue) = {
      val hits = value.asJsObject.fields("hits").asJsObject
      val count = hits.fields("total").asInstanceOf[JsNumber]
      val prices = hits.fields("hits").asInstanceOf[JsArray]
      val data = prices.elements map { h =>
        val source = h.asJsObject.fields("_source").asJsObject
        val data = source.getFields("price", "date")
        val address = source.fields("address").asJsObject
        val formattedAddress = address.getFields("primaryName",
          "street", "locality", "townCity", "county", "postcode") map { f =>
            f.asInstanceOf[JsString].value
          }

        println(formattedAddress.mkString(" "))
        PricePaidData(
          data(0).asInstanceOf[JsNumber].value.intValue(),
          parseDate(data(1).asInstanceOf[JsString].value), formattedAddress.mkString(", "))
      }
      SearchResult(count.value.intValue, data.toList)
    }

    private def parseDate(date: String) = {
      LocalDate.parse(date, dateFormat)
    }
  }
}