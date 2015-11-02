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
    val id = strip(cols(0))
    val price = strip(cols(1)).toInt
    val date = strip(cols(2))
    val postcode = Postcode(strip(cols(3)))
    val primary = strip(cols(7))
    val secondary = strip(cols(8))
    val street = strip(cols(9))
    val locality = strip(cols(10))
    val town = strip(cols(11))
    val district = strip(cols(12))
    val county = strip(cols(13))
    val location = repo.latLon(postcode)
    rowProcessor(PricePaid(id, price, date, Address(postcode.value, primary, secondary, street, locality, town, district, county, location)))
  }

  def strip(value: String) = value.replaceAll("\"", "")
}

