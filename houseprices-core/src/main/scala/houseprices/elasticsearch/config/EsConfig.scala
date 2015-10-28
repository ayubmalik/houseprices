

package houseprices.elasticsearch.config

trait EsConfig {
  def clusterName: String
  def httpEnabled = true
  def pathData = "/var/esdata/"
}

trait DevEsConfig extends EsConfig {
  def clusterName = "dev.pricepaid"
  override def pathData = "/tmp/esdata"
}

trait QaEsConfig extends EsConfig {
  def clusterName = "qa.pricepaid"
}

