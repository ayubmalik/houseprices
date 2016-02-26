package houseprices.elasticsearch

import org.elasticsearch.client.Client
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest
import org.slf4j.LoggerFactory
import org.elasticsearch.action.ActionListener
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.io.stream.StreamOutput
import org.elasticsearch.common.io.stream.OutputStreamStreamOutput
import org.elasticsearch.common.io.stream.BytesStreamOutput

class CreateIndex(val client: Client, val indexName: String, val typeName: String, mappingJsonSource: Option[String]) {

  private val log = LoggerFactory.getLogger(getClass)
  private val admin = client.admin()

  def recreate = {
    log.info("Deleting index " + indexName)
    val f = admin.indices.prepareDelete(indexName).execute
      .addListener(new ActionListener[DeleteIndexResponse] {
        def onFailure(error: Throwable) = createIndex
        def onResponse(response: DeleteIndexResponse) = createIndex
      })
  }

  def createIfNotExists = {
    val response = admin.indices.prepareExists(indexName).execute.actionGet
    if (!response.isExists) createIndex
    else
      log.info("{} index already exists...skipping", indexName)
  }

  private def createIndex = {
    log.info("Creating index " + indexName)
    admin.indices.prepareCreate(indexName)
      .setSettings(Settings.builder
        .put("index.number_of_shards", 1)
        .put("index.number_of_replicas", 0))
      .execute.actionGet

    mappingJsonSource.map { json =>
      log.info("Updating mapping...")
      val mappingRequest = new PutMappingRequest(indexName).`type`(typeName).source(json)
      admin.indices.putMapping(mappingRequest).actionGet();
    }
    admin.cluster.prepareHealth(indexName).setWaitForActiveShards(1).execute.actionGet
  }
}
