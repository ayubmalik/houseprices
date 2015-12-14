package houseprices

import scala.io.Source
import houseprices.elasticsearch.BulkAddPricePaid
import houseprices.elasticsearch.CreateIndex
import houseprices.elasticsearch.config.EsClientBuilder
import houseprices.postcodes.ClasspathSource

object CreatePricePaidIndexApp {

  def main(args: Array[String]): Unit = {

    println("Creating client")
    val client = EsClientBuilder.build()

    println("Creating index with type uk")
    val mappingJsonSource = ClasspathSource("pricepaid-uk-es-mapping.json").getLines.mkString
    new CreateIndex(client, "pricepaid", "uk", Some(mappingJsonSource)).recreate

    val csvFile = if (args.length > 0) args(0) else "tools/1000-sample-houseprices.csv"
    BulkAddPricePaid(client, csvFile).run

    println("ES up: http://localhost:9200/pricepaid/uk/_count")
  }

}