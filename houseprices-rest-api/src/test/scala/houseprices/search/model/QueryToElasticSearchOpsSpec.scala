package houseprices.search.model

import org.scalatest.Matchers
import org.scalatest.WordSpec

class QueryToElasticSearchOpsSpec extends WordSpec with Matchers {

  import QueryToElasticsearch._

  "QueryToJsonOps" should {

    "convert query to elastic search json" in {
      val qry = Query("hello mommy")
      qry.toElasticsearch should be("""{hello}""")
    }
  }
}