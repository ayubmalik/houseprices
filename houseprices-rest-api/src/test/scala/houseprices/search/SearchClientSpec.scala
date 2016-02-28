package houseprices.search

import org.scalatest.WordSpec
import org.scalatest.Matchers

class SearchClientSpeac extends WordSpec with Matchers {

  "SearchClient" should {

    "should return search result" in {
      val searchClient = HttpSearchClient()
      val result = searchClient.search(Query("hello"))
      result.count should be(0)
    }
  }

}
