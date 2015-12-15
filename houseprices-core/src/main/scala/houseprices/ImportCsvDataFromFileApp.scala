package houseprices

import houseprices.elasticsearch.BulkAddPricePaid
import houseprices.elasticsearch.config.EsClientBuilder

object ImportCsvDataFromFileApp {

  def main(args: Array[String]): Unit = {
    checkUsage(args)
    val csvFile = args(0)
    println(s"Import data from $csvFile into ES")
    val client = EsClientBuilder.transportClient
    BulkAddPricePaid(client, csvFile).run

    println("Finished import.")
    client.close()
  }

  def checkUsage(args: Array[String]) = {
    if (args.isEmpty) {
      println("usage: ImportCsvDataFromFileApp <filename>");
      System.exit(1);
    }
  }

}