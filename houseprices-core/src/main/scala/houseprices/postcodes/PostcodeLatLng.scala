package houseprices.postcodes

import houseprices.LatLon


case class Postcode(value: String) {
  def withoutSpaces = Postcode(value.replaceAll(" ", ""))
}

case class PostcodeLatLng(postcode: Postcode, latLon: LatLon)

trait PostcodeToLatLon {
  def latLon(postcode: Postcode): LatLon
}

object PostcodeRepo extends PostcodeToLatLon {

  private val Zero = LatLon(0.0, 0.0)
  lazy private val rows = new PostcodesCsv("ukpostcodes.cleaned.csv").parse()
  lazy private val data = (rows.map(p => p.postcode -> p.latLon)).toMap

  def latLon(postcode: Postcode): LatLon = {
    data.getOrElse(postcode.withoutSpaces, Zero)
  }

  def main(args: Array[String]): Unit = {
    println(PostcodeRepo.latLon(Postcode("BD94HS")))
    println(PostcodeRepo.latLon(Postcode("M130WP")))
    println(PostcodeRepo.latLon(Postcode("M210YW")))
  }
}