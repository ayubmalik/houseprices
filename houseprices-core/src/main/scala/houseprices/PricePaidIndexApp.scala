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

    val client = EsClientBuilder.build()
    
    val mappingJsonSource = Source.fromFile("src/main/resources/pricepaid-uk-es-mapping.json").getLines().mkString
    
    new CreateIndex(client, "pricepaid", "uk", Some(mappingJsonSource)).create()
    
    /*
    val bp: BulkProcessor = BulkProcessor.builder(es.client(), NoopListener).setBulkActions(10).build()

    println("Starting export")
    val prices = new PricePaidCsv(io.Source.fromFile("src/main/resources/pp-2015-part1.csv").mkString).parse()
    prices.take(100).map { pp =>
      val src = PricePaidToJson(pp)
      bp.add(new IndexRequest("pricepaid", "uk", pp.id).source(src))
    }
    */
  }

}