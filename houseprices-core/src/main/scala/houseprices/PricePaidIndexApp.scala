package houseprices

import scala.io.Source
import org.elasticsearch.action.bulk.BulkProcessor
import houseprices.csv.PricePaidCsv
import org.elasticsearch.action.index.IndexRequest
import houseprices.elasticsearch.NoopListener
import org.parboiled2.ParserInput.apply
import houseprices.elasticsearch.EmbeddedNode
import houseprices.elasticsearch.config.EsClientBuilder
import houseprices.elasticsearch.CreateIndex

object PricePaidIndexApp {

  def main(args: Array[String]): Unit = {

    println("Creating client")
    val client = EsClientBuilder.build()

    println("Creating index")
    val mappingJsonSource = Source.fromFile("src/main/resources/pricepaid-uk-es-mapping.json").getLines.mkString
    new CreateIndex(client, "pricepaid", "uk", Some(mappingJsonSource)).createIfNotExists

    println("Parsing CSV data")
    val prices = new PricePaidCsv(io.Source.fromFile("src/main/resources/pp-2015-part1.csv").mkString).parse

    println("Starting bulk add")
    val bulk = BulkProcessor.builder(client, NoopListener).setBulkActions(10).build()
    
    prices.map { pp =>
      val src = PricePaidToJson(pp)
      bulk.add(new IndexRequest("pricepaid", "uk", pp.id).source(src))
    }

  }

}