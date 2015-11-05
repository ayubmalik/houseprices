package houseprices.csv

import houseprices.postcodes.ClasspathSource
import houseprices.postcodes.Postcode
import houseprices.postcodes.PostcodeRepo
import houseprices.PricePaid
import houseprices.Address
import houseprices.postcodes.FileSource

class PricePaidCsvProcessor(val csvInputFile: String) {
  lazy val postcodeRepo = PostcodeRepo
  lazy val csv = FileSource(csvInputFile, "iso-8859-1").getLines()

  var lineNumber = 0

  def foreach(rowProcessor: PricePaid => Unit): Unit = {
    for (row <- csv)
      processRow(row, rowProcessor)
  }

  private def processRow(rowString: String, pricePaidProcessor: PricePaid => Unit) = {
    try {
      lineNumber = lineNumber + 1
      val cols = rowString.split(",").map(_.replaceAll("\"", ""))
      val id = cols(0)
      val price = cols(1).toInt
      val date = cols(2)
      val postcode = Postcode(cols(3))
      val primary = cols(7)
      val secondary = cols(8)
      val street = cols(9)
      val locality = cols(10)
      val town = cols(11)
      val district = cols(12)
      val county = cols(13)
      val location = postcodeRepo.latLon(postcode)
      pricePaidProcessor(PricePaid(id, price, date, Address(postcode.value, primary, secondary, street, locality, town, district, county, location)))
    } catch {
      case e: Exception => {
        println(s"lineNumber = $lineNumber")
        e.printStackTrace
      }
    }
  }

}

