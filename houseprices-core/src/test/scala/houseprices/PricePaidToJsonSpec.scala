package houseprices

import org.scalatest.FlatSpec
import org.scalatest.Matchers

class PricePaidToJsonSpec extends FlatSpec with Matchers {

  "HousePriceToJson" should "convert to json" in {
    val housePrice = PricePaid("123", 55000, "2004", Address("primaryx", "secondaryx", "streetx", "localityx", "townx", "districtx", "countyx"))
    PricePaidToJson(housePrice) should be(
      """{
          "id" : "123"
        }""")
  }
}