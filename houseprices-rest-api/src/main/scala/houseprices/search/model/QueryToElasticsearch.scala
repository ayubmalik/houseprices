package houseprices.search.model

import scala.language.implicitConversions

object QueryToElasticsearch {

  val queryTemplate = """{"query": {"query_string": {"query": "_text_"}}}"""

  implicit class ElasticSearch(val qry: Query) {
    def toElasticsearch = queryTemplate.replaceAll("_text_", qry.text)
  }
}