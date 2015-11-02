package houseprices.csv

import houseprices.postcodes.ClasspathSource
import houseprices.postcodes.Postcode
import houseprices.postcodes.PostcodeRepo
import houseprices.PricePaid
import houseprices.Address

class PricePaidCsvProcessor(val csvInputFile: String) {
  lazy val repo = PostcodeRepo
  lazy val csv = ClasspathSource(csvInputFile).getLines()

  def foreach(rowProcessor: PricePaid => Unit): Unit = {
    for (row <- csv)
      processRow(row, rowProcessor)
  }

  private def processRow(rowString: String, rowProcessor: PricePaid => Unit) = {
    val cols = rowString.split(",")
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
    val location = repo.latLon(postcode)
    rowProcessor(PricePaid(id, price, date, Address(postcode.value, primary, secondary, street, locality, town, district, county, location)))
  }
}

