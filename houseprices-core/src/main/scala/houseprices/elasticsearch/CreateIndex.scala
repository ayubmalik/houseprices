package houseprices.elasticsearch

import org.elasticsearch.client.Client
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest
import org.slf4j.LoggerFactory

class CreateIndex(val client: Client, val indexName: String, val typeName: String, mappingJsonSource: Option[String]) {

  val log = LoggerFactory.getLogger(getClass)
  private val admin = client.admin()

  def recreate = {
    log.info("Deleting index " + indexName)
    admin.indices.prepareDelete(indexName).execute.actionGet
    createIndex
  }

  def createIfNotExists = {
    val response = admin.indices.prepareExists(indexName).execute.actionGet
    if (!response.isExists) {
      createIndex
    } else log.info("{} index already exists...skipping", indexName)
  }

  private def createIndex = {
    log.info("Creating index " + indexName)
    admin.indices.prepareCreate(indexName).execute.actionGet
    mappingJsonSource.map { json =>
      val mappingRequest = new PutMappingRequest(indexName).`type`(typeName).source(json)
      admin.indices.putMapping(mappingRequest).actionGet();
    }
    admin.cluster.prepareHealth(indexName).setWaitForActiveShards(1).execute.actionGet
  }
}
