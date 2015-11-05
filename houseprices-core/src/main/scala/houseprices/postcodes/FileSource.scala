package houseprices.postcodes

import java.io.File

object FileSource {

  def apply(name: String): io.Source = io.Source.fromFile(name)
  def apply(name: String, enc: String): io.Source = io.Source.fromFile(name, enc)

}
