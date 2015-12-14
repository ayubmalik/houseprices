package houseprices.csv

import houseprices.postcodes.ClasspathSource
import houseprices.postcodes.Postcode
import houseprices.postcodes.PostcodeRepo
import houseprices.PricePaid
import houseprices.Address
import houseprices.postcodes.FileSource
import org.apache.http.client.HttpClient
import org.apache.http.impl.client.DefaultHttpClient

object PricePaidDataFetcher {

  val fullCsvFile = "http://publicdata.landregistry.gov.uk/market-trend-data/price-paid-data/b/pp-complete.csv"

  def fetchFullCsv = {
    val httpClient = new DefaultHttpClient()
  }

}

