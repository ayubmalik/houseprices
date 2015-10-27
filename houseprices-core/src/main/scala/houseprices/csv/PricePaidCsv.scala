package houseprices.csv

import org.parboiled2.ParserInput

class PricePaidCsv(val input: ParserInput) extends CSVParboiledParser with CSVParserIETFAction {
  def parse(): List[List[String]] = csvfile.run().get
}

object PricePaidCsvApp {

  def main(args: Array[String]): Unit = {
    val csvFile = "src/main/resources/pp-2015-part1.csv"
    val rows = new PricePaidCsv(io.Source.fromFile(csvFile).mkString).parse()
    println(rows.size)
  }
}