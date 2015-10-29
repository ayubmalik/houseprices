package houseprices

import scala.io.Source

import org.elasticsearch.action.bulk.BulkProcessor
import org.elasticsearch.action.index.IndexRequest
import org.parboiled2.ParserInput.apply

import houseprices.csv.PricePaidCsv
import houseprices.elasticsearch.CreateIndex
import houseprices.elasticsearch.NoopListener
import houseprices.elasticsearch.config.EsClientBuilder

object PricePaidIndexApp {

  def main(args: Array[String]): Unit = {

    println("Creating client")
    val client = EsClientBuilder.build()

    println("Creating index")
    val mappingJsonSource = Source.fromFile("src/main/resources/pricepaid-uk-es-mapping.json").getLines.mkString
    new CreateIndex(client, "pricepaid", "uk", Some(mappingJsonSource)).recreate

    println("Parsing CSV data")
    val prices = new PricePaidCsv(io.Source.fromFile("src/main/resources/pp-2015-part1.csv").mkString).parse

    println("Starting bulk add")
    val bulk = BulkProcessor.builder(client, NoopListener).setBulkActions(100).build()

    prices.map { pp =>
      val src = PricePaidToJson(pp)
      bulk.add(new IndexRequest("pricepaid", "uk", pp.id).source(src))
    }

  }

}