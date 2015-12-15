package houseprices

import scala.io.Source
import houseprices.elasticsearch.BulkAddPricePaid
import houseprices.elasticsearch.CreateIndex
import houseprices.elasticsearch.config.EsClientBuilder
import houseprices.postcodes.ClasspathSource

object ImportCsvDataFromFileApp {

  def main(args: Array[String]): Unit = {
    checkUsage(args)
    val csvFile = args(0)
    println(s"Import data from $csvFile into ES")
    BulkAddPricePaid(EsClientBuilder.build(), csvFile).run

    println("ES up: http://localhost:9200/pricepaid/uk/_count")
  }

  def checkUsage(args: Array[String]) = {
    if (args.isEmpty) {
      println("usage: ImportCsvDataFromFileApp <filename>");
      System.exit(1);
    }
  }

}