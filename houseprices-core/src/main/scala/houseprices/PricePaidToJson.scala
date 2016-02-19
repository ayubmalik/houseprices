package houseprices

import spray.json.DefaultJsonProtocol

object PricePaidToJson extends DefaultJsonProtocol {
  implicit val latLon = jsonFormat2(LatLon)
  implicit val address = jsonFormat9(Address)
  implicit val pp = jsonFormat4(PricePaid)

  import spray.json._

  def apply(pricePaid: PricePaid) = {
    val p =  pricePaid.toJson.toString()
    println (p)
    p
  }
}

