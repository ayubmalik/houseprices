package houseprices

import org.json4s.JsonDSL._
import org.json4s._
import org.json4s.native.Serialization
import org.json4s.native.Serialization.{ read, write }

object PricePaidToJson {
  implicit val formats = Serialization.formats(NoTypeHints)

  def apply(pricePaid: PricePaid) = write(pricePaid)
}

