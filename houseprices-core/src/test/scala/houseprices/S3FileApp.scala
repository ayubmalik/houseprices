package houseprices

import scala.io.Source
import java.time.Period
import java.time.LocalDate
import java.time.temporal.TemporalUnit
import java.time.temporal.ChronoUnit
import java.time.LocalDateTime

object S3FileApp extends App {
  val url1 = "http://prod.publicdata.landregistry.gov.uk.s3-website-eu-west-1.amazonaws.com/pp-monthly-update-new-version.csv"
  val url2 = "http://prod.publicdata.landregistry.gov.uk.s3-website-eu-west-1.amazonaws.com/pp-complete.csv"

  lazy val lines = Source.fromURL(url2).getLines

  val start = LocalDateTime.now
  val count = lines.size
  val end = LocalDateTime.now
  val seconds = ChronoUnit.MILLIS.between(start, end) / 1000.0
  println("\nCount: " + count + " took: " + seconds + "s")
}