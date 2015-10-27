package houseprices.elasticsearch

import org.elasticsearch.action.bulk.BulkProcessor
import org.elasticsearch.action.bulk.BulkRequest
import org.elasticsearch.action.bulk.BulkResponse

object NoopListener extends BulkProcessor.Listener {

  def beforeBulk(id: Long, re: BulkRequest): Unit = {
  }

  def afterBulk(id: Long, req: BulkRequest, res: BulkResponse): Unit = {
  }

  def afterBulk(id: Long, req: BulkRequest, err: Throwable): Unit = {
  }

}