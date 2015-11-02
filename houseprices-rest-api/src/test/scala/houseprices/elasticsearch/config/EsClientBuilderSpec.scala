package houseprices.elasticsearch.config

import org.scalatest.Matchers
import org.scalatest.FlatSpec

class EsClientBuilderSpec extends FlatSpec with Matchers {

  "EsClientBuilder" should "build for dev by default" in {
    val client = EsClientBuilder.build()
    client.settings().get("cluster.name") should be("dev.pricepaid")
    client.close()
  }
  
  "EsClientBuilder" should "build for qa by when specified" in {
    val client = EsClientBuilder.build("qa")
    client.settings().get("cluster.name") should be("qa.pricepaid")
    client.close()
  }
}