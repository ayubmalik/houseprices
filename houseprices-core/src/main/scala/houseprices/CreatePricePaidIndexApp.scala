package houseprices

import houseprices.elasticsearch.BulkImportPricePaidData
import houseprices.elasticsearch.CreateIndex
import houseprices.elasticsearch.config.EsClientBuilder
import houseprices.postcodes.ClasspathSource

object CreatePricePaidIndexApp {

  def main(args: Array[String]): Unit = {

    println("Creating ES client")
    val client = EsClientBuilder.buildClient()

    println("Creating ES index with type uk")
    val mappingJsonSource = ClasspathSource("pricepaid-uk-es-mapping.json").getLines.mkString
    new CreateIndex(client, "pricepaid", "uk", Some(mappingJsonSource)).recreate

    val csvFile = if (args.length > 0) args(0) else "tools/1000-sample-houseprices.csv"
    BulkImportPricePaidData(client, csvFile).run

    println("ES up: http://localhost:9200/pricepaid/uk/_count")
  }

}