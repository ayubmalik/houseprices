package houseprices.elasticsearch

import java.util.concurrent.TimeUnit
import org.elasticsearch.action.bulk.BulkProcessor
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.client.Client
import houseprices.PricePaidToJson
import houseprices.csv.PricePaidCsvProcessor
import org.slf4j.LoggerFactory

class BulkAddPricePaid(val client: Client, val csvFile: String) {

  val log = LoggerFactory.getLogger(getClass)

  def run() = {

    log.info("Creating es bulk processor")
    val bulk = BulkProcessor.builder(client, NoopListener).setBulkActions(1000).build()

    log.info("Creating csv processor for file: " + csvFile)
    val csv = new PricePaidCsvProcessor(csvFile)
    csv.foreach { pp =>
      val json = PricePaidToJson(pp)
      bulk.add(new IndexRequest("pricepaid", "uk", pp.id).source(json))
    }
    bulk.flush
    val status = bulk.awaitClose(3000, TimeUnit.MILLISECONDS)
    log.info("Closed bulk processor. Success status: " + status)
  }
}

object BulkAddPricePaid {
  def apply(client: Client, csvFile: String) = new BulkAddPricePaid(client, csvFile)
}
