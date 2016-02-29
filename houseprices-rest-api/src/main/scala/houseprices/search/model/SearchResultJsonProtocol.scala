package houseprices.search.model
import spray.json.DefaultJsonProtocol
import spray.json.JsArray
import spray.json.RootJsonFormat
import spray.json.JsString
import spray.json.JsNumber
import spray.json._

object SearchResultJsonProtocol extends DefaultJsonProtocol {

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
        PricePaidData(data(0).asInstanceOf[JsNumber].value.intValue(), data(1).toString)
      }
      SearchResult(count.value.intValue, data.toList)
    }
  }
}