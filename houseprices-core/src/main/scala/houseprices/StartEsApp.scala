package houseprices

import houseprices.elasticsearch.config.EsClientBuilder

object StartEsOnlyApp {

  def main(args: Array[String]): Unit = {
    println("Creating ES client")
    EsClientBuilder.buildClient()

    println("ES up: http://localhost:9200/pricepaid/uk/_count")
  }

}