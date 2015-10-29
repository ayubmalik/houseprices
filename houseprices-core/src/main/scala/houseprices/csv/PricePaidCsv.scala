package houseprices.csv

import org.parboiled2.ParserInput
import houseprices.PricePaid
import houseprices.Address
import houseprices.postcodes.LatLng

class PricePaidCsv(val input: ParserInput) extends CSVParboiledParser with CSVParserIETFAction {
  def parse(): List[PricePaid] = {
    val rows = csvfile.run().get
    rows.map { fields =>
      val id = fields(0)
      val price = fields(1).toInt
      val date = fields(2)
      val postcode = fields(3)
      val primary = fields(7)
      val secondary = fields(8)
      val street = fields(9)
      val locality = fields(10)
      val town = fields(11)
      val district = fields(12)
      val county = fields(13)
      PricePaid(id, price, date, Address(postcode, primary, secondary, street, locality, town, district, county, LatLng(0, 0)))
    }
  }
}

object PricePaidCsvApp {
  def main(args: Array[String]): Unit = {
    val csvFile = "src/main/resources/pp-2015-part1.csv"
    val rows = new PricePaidCsv(io.Source.fromFile(csvFile).mkString).parse()
    println(rows.size)
  }
}