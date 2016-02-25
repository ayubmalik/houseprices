package houseprices.elasticsearch.config

trait EsConfig {
  def clusterName: String
  def httpEnabled = true
  def pathHome = "/var/elasticsearch"
  def pathData = pathHome + "/data"
  def isLocal = false
}

trait DevConfig extends EsConfig {
  def clusterName = "dev.pricepaid"
  override def isLocal = true
  override def pathHome = "/tmp/elasticsearch"
}

trait QaConfig extends EsConfig {
  def clusterName = "qa.pricepaid"
}



