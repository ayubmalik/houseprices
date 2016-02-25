package houseprices.elasticsearch.config

import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.node.NodeBuilder
import org.elasticsearch.common.settings.Settings
import java.net.InetAddress
import org.elasticsearch.node.Node

trait EsClientBuilder {
  this: EsConfig =>

  val settings = Settings.settingsBuilder()
    .put("http.enabled", this.httpEnabled)
    .put("path.home", this.pathHome)
    .put("path.data", this.pathData)
    .put("node.local", this.isLocal)
    .put("node.client", this.isClient)
    .put("node.name", "node." + this.clusterName)

  def createNode: Node = {
    NodeBuilder.nodeBuilder()
      .clusterName(clusterName)
      .settings(settings)
      .build()
  }

  def build: Client = {
    createNode.start().client()
  }
}

object EsClientBuilder {
  def buildClient(env: String = "dev"): Client = {
    env match {
      case "qa" => (new EsClientBuilder with QaConfig) build
      case _ => (new EsClientBuilder with DevConfig) build
    }
  }

  def transportClient = {
    val settings = Settings.settingsBuilder().put("cluster.name", "dev.pricepaid")
    TransportClient.builder().settings(settings).build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getLocalHost, 9300))
  }

}
