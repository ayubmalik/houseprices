package houseprices

import scala.io.Source

object S3FileApp extends App {
  val url1 = "http://prod.publicdata.landregistry.gov.uk.s3-website-eu-west-1.amazonaws.com/pp-monthly-update-new-version.csv"
  val url2 = "http://prod.publicdata.landregistry.gov.uk.s3-website-eu-west-1.amazonaws.com/pp-complete.csv"

  lazy val lines = Source.fromURL(url2).getLines

  var count = 0
  //  for (row <- lines) {
  //    println(row)
  //    count += 1
  //  }
  count = lines.size
  println("\n\nCount: " + count)
}