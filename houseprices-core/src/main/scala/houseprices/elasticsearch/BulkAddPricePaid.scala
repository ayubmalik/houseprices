package houseprices.elasticsearch

import java.util.concurrent.TimeUnit

import org.elasticsearch.action.bulk.BulkProcessor
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.client.Client

import houseprices.PricePaidToJson
import houseprices.csv.PricePaidCsvProcessor

class BulkAddPricePaid(val client: Client, val csvFile: String) {

  def run() = {

    println("Creating es bulk processor")
    val bulk = BulkProcessor.builder(client, PrintListener).setBulkActions(1000).build()

    println("Creating csv processor for file: " + csvFile)
    val csv = new PricePaidCsvProcessor(csvFile)
    csv.foreach { pp =>
      val json = PricePaidToJson(pp)
      bulk.add(new IndexRequest("pricepaid", "uk", pp.id).source(json))
    }

    val status = bulk.awaitClose(3000, TimeUnit.MILLISECONDS)
    println("\nClosed bulk processor. Success status: " + status)
  }
}

object BulkAddPricePaid {
  def apply(client: Client, csvFile: String) = new BulkAddPricePaid(client, csvFile)
}
