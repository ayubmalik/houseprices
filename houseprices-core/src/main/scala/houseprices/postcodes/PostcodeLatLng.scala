package houseprices.postcodes

case class LatLng(lat: Double, lng: Double)

case class Postcode(value: String) {
  def withoutSpaces = Postcode(value.replaceAll(" ", ""))
}

case class PostcodeLatLng(postcode: Postcode, latlng: LatLng)

trait PostcodeToLatLng {
  def latLng(postcode: Postcode): LatLng
}

object PostcodeRepo extends PostcodeToLatLng {

  private val Zero = LatLng(0.0, 0.0)
  lazy private val rows = new PostcodesCsv(ClasspathSource("ukpostcodes.cleaned.csv").mkString).parse()
  lazy private val data = (rows.map(p => p.postcode -> p.latlng)).toMap

  def latLng(postcode: Postcode): LatLng = {
    data.getOrElse(postcode.withoutSpaces, Zero)
  }

  def main(args: Array[String]): Unit = {
    println(PostcodeRepo.latLng(Postcode("BD94HS")))
    println(PostcodeRepo.latLng(Postcode("M130WP")))
    println(PostcodeRepo.latLng(Postcode("M210YW")))
  }
}