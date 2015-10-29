package houseprices.postcodes

import org.parboiled2.ParserInput
import houseprices.csv.CSVParboiledParser
import houseprices.csv.CSVParserIETFAction

class PostcodesCsv(val input: ParserInput) extends CSVParboiledParser with CSVParserIETFAction {

  def parse(): List[PostcodeLatLng] = {
    val rows = csvfile.run().get
    rows.map { fields =>
      val postcode = fields(1)
      val lat = fields(2)
      val lng = fields(3)
      PostcodeLatLng(Postcode(postcode), LatLng(lat, lng))
    }
  }
}

object PricePaidCsvApp {

  def main(args: Array[String]): Unit = {
    val csvFile = "src/main/resources/pp-2015-part1.csv"
    val rows = new PostcodesCsv(io.Source.fromFile(csvFile).mkString).parse()
    println(rows.size)
  }
}