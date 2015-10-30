package houseprices

import org.parboiled2._

import houseprices.csv.CSVParboiledParser
import houseprices.csv.CSVParserIETFAction
import houseprices.postcodes.Postcode
import houseprices.postcodes.PostcodeRepo

class PricePaidCsv(val input: ParserInput) extends CSVParboiledParser with CSVParserIETFAction {
  val repo = PostcodeRepo
  
  def parse(): List[PricePaid] = {
    val rows = csvfile.run().get
    rows.map { fields =>
      val id = fields(0)
      val price = fields(1).toInt
      val date = fields(2)
      val postcode = Postcode(fields(3))
      val primary = fields(7)
      val secondary = fields(8)
      val street = fields(9)
      val locality = fields(10)
      val town = fields(11)
      val district = fields(12)
      val county = fields(13)
      val location = repo.latLon(postcode)
      PricePaid(id, price, date, Address(postcode.value, primary, secondary, street, locality, town, district, county, location))
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