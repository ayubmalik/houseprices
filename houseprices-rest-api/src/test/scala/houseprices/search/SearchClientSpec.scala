package houseprices.search

import scala.concurrent.Future

import org.scalatest.Matchers
import org.scalatest.WordSpec

import akka.actor.ActorSystem
import akka.http.scaladsl.model.ContentType.apply
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.MediaTypes
import houseprices.search.model.Query

class SearchClientSpec extends WordSpec with Matchers {

  implicit val system = ActorSystem("test")
  implicit val ec = system.dispatcher

  trait StubHttpRequestFactory extends HttpRequestFactory {
    import MediaTypes.`application/json`
    val data = """{"hits":{"total":0,"max_score":null,"hits":[]}}"""
    def request(req: HttpRequest) = Future[HttpResponse] {
      HttpResponse(entity = HttpEntity(`application/json`, data))
    }
  }

  "SearchClient" when {

    "searching" should {

      "return search result" in {
        val searchClient = new HttpSearchClient with StubHttpRequestFactory
        val result = searchClient.search(Query("hello"))
        result map { _.count should be(0) }

      }

      "return count of two" ignore {
        val searchClient = new HttpSearchClient with StubHttpRequestFactory
        val result = searchClient.search(Query("world"))
        result map { _.count should be(2) }
      }
    }
  }
}
