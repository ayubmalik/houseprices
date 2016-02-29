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

object SearchResultJsonProtocol extends DefaultJsonProtocol {

  private val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

  implicit object SearchResultJsonFormat extends RootJsonFormat[SearchResult] {
    def write(s: SearchResult) =
      JsArray(JsNumber(s.count))

    def read(value: JsValue) = {
      val outerHits = value.asJsObject.fields("hits").asJsObject
      val count = outerHits.fields("total").asInstanceOf[JsNumber]
      val prices = outerHits.fields("hits").asInstanceOf[JsArray]
      val data = prices.elements map { h =>
        val source = h.asJsObject.fields("_source").asJsObject
        val data = source.getFields("price", "date")
        PricePaidData(
          data(0).asInstanceOf[JsNumber].value.intValue(),
          parseDate(data(1).asInstanceOf[JsString].value))
      }
      SearchResult(count.value.intValue, data.toList)
    }

    private def parseDate(date: String) = {
      LocalDate.parse(date, dateFormat)
    }
  }
}