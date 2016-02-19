package houseprices.elasticsearch.config

trait EsConfig {
  def clusterName: String
  def httpEnabled = true
  def pathData = "/var/esdata/"
  def isLocal = false
}

trait DevConfig extends EsConfig {
  def clusterName = "dev.pricepaid"
  override def pathData = "/tmp/esdata"
}

trait LocalConfig extends EsConfig {
  def clusterName = "dev.pricepaid"
  override def isLocal = false
}

trait QaConfig extends EsConfig {
  def clusterName = "qa.pricepaid"
  override def isLocal = false
}



