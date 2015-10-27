package houseprices

import org.scalatest.FlatSpec
import org.scalatest.Matchers

class HousePriceToJsonSpec extends FlatSpec with Matchers {

  "HousePriceToJson" should "convert to json" in {
    val housePrice = HousePrice("123", 55000, "2004", Address("primaryx", "secondaryx", "streetx", "localityx", "townx", "districtx", "countyx"))
    HousePriceToJson(housePrice) should be(
     """{
          "id" : "123"
        }""")
  }
}