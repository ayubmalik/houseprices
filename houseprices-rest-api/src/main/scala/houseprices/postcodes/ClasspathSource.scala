package houseprices.postcodes

import java.io.File

object ClasspathSource {

  def apply(name: String): io.Source = {
    val safeName = if (name.startsWith(File.separator)) name else (File.separator + name)
    io.Source.fromURL(getClass.getResource(safeName), "ISO-8859-1")
  }

}
