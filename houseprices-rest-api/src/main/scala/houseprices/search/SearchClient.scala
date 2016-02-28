package houseprices.search

case class Query(query: String)

trait SearchClient {
  def search(query: Query): SearchResult
}

class HttpSearchClient extends SearchClient {
  def search(query: Query) = SearchResult(0)
}

object HttpSearchClient {
  def apply() = new HttpSearchClient
}
