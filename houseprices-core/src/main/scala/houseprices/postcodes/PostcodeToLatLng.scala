package houseprices.postcodes

import houseprices.csv.CSVParboiledParser

trait PostcodeToLatLng {
  def latLng(postcode:String): LatLng
}

object PostcodeRepo extends PostcodeToLatLng {
  
  
  def latLng(postcode: String): LatLng = {
    ???
  }
}