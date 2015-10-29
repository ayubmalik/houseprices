package houseprices.postcodes

import org.parboiled2.ParserInput
import houseprices.csv.CSVParboiledParser
import houseprices.csv.CSVParserIETFAction

class PostcodesCsv(val input: ParserInput) extends CSVParboiledParser with CSVParserIETFAction {

  def parse(): List[PostcodeLatLng] = {
    val rows = csvfile.run().get
    rows.map { fields =>
      val postcode = fields(1)
      val lat = fields(2).toDouble
      val lng = fields(3).toDouble
      PostcodeLatLng(Postcode(postcode), LatLng(lat, lng))
    }
  }
}

object PostcodesCsvApp {

  def main(args: Array[String]): Unit = {
    val csvFile = ClasspathSource("ukpostcodes.cleaned.csv")
    val rows = new PostcodesCsv(csvFile.mkString).parse()
    println(rows(2))
  }
}