package houseprices.csv

import org.parboiled2.ParserInput

class HousePricesCsv(val input: ParserInput) extends CSVParboiledParser with CSVParserIETFAction {
  def parse(): List[List[String]] = csvfile.run().get
}

object HousePricesCsvApp {

  def main(args: Array[String]): Unit = {
    val csvFile = "src/main/resources/pp-2015-part1.csv"
    val rows = new HousePricesCsv(io.Source.fromFile(csvFile).mkString).parse()
    println(rows.size)
  }
}