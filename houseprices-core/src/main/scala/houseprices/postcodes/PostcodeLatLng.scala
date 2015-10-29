package houseprices.postcodes

case class LatLng(lat: String, lng: String)

case class Postcode(value: String)

case class PostcodeLatLng(postcode: Postcode, latlng: LatLng)