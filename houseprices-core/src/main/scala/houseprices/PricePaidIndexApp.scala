package houseprices

import scala.io.Source

import houseprices.elasticsearch.BulkAddPricePaid
import houseprices.elasticsearch.CreateIndex
import houseprices.elasticsearch.config.EsClientBuilder

object PricePaidIndexApp {

  def main(args: Array[String]): Unit = {

    println("Creating client")
    val client = EsClientBuilder.build()

    println("Creating index")
    val mappingJsonSource = Source.fromFile("src/main/resources/pricepaid-uk-es-mapping.json").getLines.mkString
    new CreateIndex(client, "pricepaid", "uk", Some(mappingJsonSource)).recreate

    BulkAddPricePaid(client, "pp-monthly-update.txt").run

  }

}