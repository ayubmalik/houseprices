package houseprices.postcodes

import scala.io.Source
import houseprices.LatLon

class PostcodesCsv(val csvInputFile: String) {

  def parse(): List[PostcodeLatLng] = {
    val rows = ClasspathSource(csvInputFile).getLines
    val postcodes = for (row <- rows) yield processRow(row)
    postcodes.toList
  }

  private def processRow(row: String) = {
    val cols = row.split(",")
    val postcode = cols(1)
    val lat = cols(2).toDouble
    val lng = cols(3).toDouble
    PostcodeLatLng(Postcode(postcode), LatLon(lat, lng))
  }
}

object PostcodesCsvApp {

  def main(args: Array[String]): Unit = {
    val rows = new PostcodesCsv("ukpostcodes.cleaned.csv").parse()
    println(rows(2))
  }
}