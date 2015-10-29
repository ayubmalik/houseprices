package houseprices

import org.scalatest.FlatSpec
import org.scalatest.Matchers
import houseprices.postcodes.LatLon

class PricePaidToJsonSpec extends FlatSpec with Matchers {

  "HousePriceToJson" should "convert to json" in {
    val housePrice = PricePaid("123", 5000, "2004", Address("pcode", "p", "s", "st", "l", "t", "d", "c", LatLon(0, 0)))
    PricePaidToJson(housePrice) should be(
      """{"id":"123","price":5000,"date":"2004","address":{"postcode":"pcode","primaryName":"p","secondaryName":"s","street":"st","locality":"l","townCity":"t","district":"d","county":"c","location":{"lat":0.0,"lon":0.0}}}""")
  }
}