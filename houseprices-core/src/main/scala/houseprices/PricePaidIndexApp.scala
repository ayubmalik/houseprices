package houseprices

import scala.io.Source
import org.elasticsearch.action.bulk.BulkProcessor
import org.elasticsearch.action.index.IndexRequest
import org.parboiled2.ParserInput.apply
import houseprices.elasticsearch.CreateIndex
import houseprices.elasticsearch.NoopListener
import houseprices.elasticsearch.config.EsClientBuilder
import houseprices.elasticsearch.BulkAddPricePaid

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