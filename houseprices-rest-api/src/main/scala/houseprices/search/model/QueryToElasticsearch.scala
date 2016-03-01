package houseprices.search.model

import scala.language.implicitConversions

object QueryToElasticsearch {

  implicit class ElasticSearch(val qry: Query) {
    def toElasticsearch = """{hello}"""
  }
}