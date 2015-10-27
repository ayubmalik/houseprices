package houseprices.elasticsearch

import org.scalatest.FlatSpec

class CreateHousePricesIndexSpec extends FlatSpec {
  
  val esNode = EmbeddedNode()
  
  "Elastic Search" should "serve docs" in {
    esNode.start()
    
    esNode.shutdown(true)
    
  }
  
}