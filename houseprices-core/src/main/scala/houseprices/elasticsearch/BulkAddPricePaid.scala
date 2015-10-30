package houseprices.elasticsearch

import java.util.concurrent.TimeUnit
import org.elasticsearch.action.bulk.BulkProcessor
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.client.Client
import org.elasticsearch.client.Requests
import org.parboiled2.ParserInput.apply
import houseprices.PricePaidCsv
import houseprices.PricePaidToJson
import houseprices.postcodes.ClasspathSource
import org.elasticsearch.common.unit.TimeValue

class BulkAddPricePaid(val client: Client, val csvFile: String) {

  def run() = {
    println("Parsing CSV file: " + csvFile)
    val rows = new PricePaidCsv(ClasspathSource(csvFile).mkString).parse

    println("Starting bulk add")
    val bulk = BulkProcessor
      .builder(client, PrintListener).setBulkActions(1000).build()

    rows.foreach { pp =>
      val json = PricePaidToJson(pp)
      bulk.add(new IndexRequest("pricepaid", "uk", pp.id).source(json))
    }
    val status = bulk.awaitClose(3000, TimeUnit.MILLISECONDS)
    println("\nFlush status: " + status)
  }
}

object BulkAddPricePaid {
  def apply(client: Client, csvFile: String) = new BulkAddPricePaid(client, csvFile)
}
