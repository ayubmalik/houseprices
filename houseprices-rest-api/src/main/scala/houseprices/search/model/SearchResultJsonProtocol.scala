package houseprices.search.model
import spray.json.DefaultJsonProtocol

trait SearchResultJsonProtool extends DefaultJsonProtocol {
  implicit val format1 = jsonFormat1(PricePaid)
}