package houseprices

import scala.io.Source
import org.elasticsearch.action.bulk.BulkProcessor
import houseprices.csv.PricePaidCsv
import org.elasticsearch.action.index.IndexRequest
import houseprices.elasticsearch.NoopListener
import org.parboiled2.ParserInput.apply
import houseprices.elasticsearch.EmbeddedNode

object CreatePricePaidIndexApp {

  def main(args: Array[String]): Unit = {

    val mappingJson = Source.fromFile("src/main/resources/pricepaid-uk-es-mapping.json").getLines().mkString

    val es = EmbeddedNode()
    es.start();
    es.createAndWaitForIndex("pricepaid", Some(mappingJson))

    val bp: BulkProcessor = BulkProcessor.builder(es.client(), NoopListener).setBulkActions(10).build()

    println("Starting export")
    val prices = new PricePaidCsv(io.Source.fromFile("src/main/resources/pp-2015-part1.csv").mkString).parse()
    prices.map { pp =>
      val src = PricePaidToJson(pp)
      bp.add(new IndexRequest("pricepaid", "uk", pp.id).source(src))
    }

    //es.shutdown(false)
  }

}