package houseprices.elasticsearch.config

import org.elasticsearch.client.Client
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.node.NodeBuilder

trait EsClientBuilder {
  this: EsConfig =>
  def build: Client = {
    val settings = ImmutableSettings.settingsBuilder()
      .put("http.enabled", this.httpEnabled)
      .put("path.data", this.pathData)
      .put("node.name", "node." + this.clusterName)

    NodeBuilder.nodeBuilder()
      .local(this.isLocal)
      .clusterName(clusterName)
      .settings(settings)
      .build()
        .start()
        .client()
  }
}

object EsClientBuilder {
  def build(env: String = "dev"): Client = {
    env match {
      case "qa" => new EsClientBuilder with QaConfig build
      case _ => new EsClientBuilder with DevConfig build
    }
  }
}