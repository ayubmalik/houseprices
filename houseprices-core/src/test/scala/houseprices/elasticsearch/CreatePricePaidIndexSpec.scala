package houseprices.elasticsearch

import org.scalatest.FlatSpec

class CreatePricePaidIndexSpec extends FlatSpec {

  val esNode = EmbeddedNode()

  "Elastic Search" should "serve docs" in {
    esNode.start()

    esNode.shutdown(true)

  }

}