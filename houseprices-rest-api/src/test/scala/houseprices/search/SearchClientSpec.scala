package houseprices.search

import org.scalatest.WordSpec
import org.scalatest.Matchers
import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpEntity, HttpRequest, HttpResponse, MediaTypes}
import scala.concurrent.Future

class SearchClientSpec extends WordSpec with Matchers {

  implicit val system = ActorSystem("test")
  implicit val ec = system.dispatcher

  trait StubHttpRequestFactory extends HttpRequestFactory {
    import MediaTypes.`application/json`
    val data = """{ "hits": 0 }"""
    def request(req: HttpRequest) = Future[HttpResponse] {
      HttpResponse(entity = HttpEntity(`application/json`, data))}
  }

  "SearchClient" when {

    "searching" should {

      "return search result" in {
        val searchClient = new HttpSearchClient with StubHttpRequestFactory
        val result = searchClient.search(Query("hello"))
        result.count should be(0)
      }

      "return count of two" ignore {
        val searchClient = new HttpSearchClient with StubHttpRequestFactory
        val result = searchClient.search(Query("world"))
        result.count should be(2)
      }

    }
  }
}
