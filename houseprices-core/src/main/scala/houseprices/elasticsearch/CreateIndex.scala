package houseprices.elasticsearch

import org.elasticsearch.client.Client
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest

class CreateIndex(val client: Client, val indexName: String, val typeName: String, mappingJsonSource: Option[String]) {
  private val admin = client.admin()

  def create() = {
    admin.indices().prepareDelete(indexName).execute().actionGet()

    admin.indices.prepareCreate(indexName).execute.actionGet()

    mappingJsonSource.map { json =>
      val mappingRequest = new PutMappingRequest(indexName).`type`(typeName).source(json)
      admin.indices.putMapping(mappingRequest).actionGet();
    }

    admin.cluster.prepareHealth(indexName).setWaitForActiveShards(1).execute.actionGet()
  }

}