package houseprices

import scala.io.Source

import houseprices.elasticsearch.BulkAddPricePaid
import houseprices.elasticsearch.CreateIndex
import houseprices.elasticsearch.config.EsClientBuilder

object StartEsOnlyApp {

  def main(args: Array[String]): Unit = {

    println("Creating client")
    val client = EsClientBuilder.build()

    

  }

}