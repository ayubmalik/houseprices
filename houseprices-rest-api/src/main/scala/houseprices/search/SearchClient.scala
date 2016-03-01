package houseprices.search

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ HttpMethods, HttpRequest, HttpResponse }
import akka.http.scaladsl.unmarshalling.Unmarshal
import org.slf4j.LoggerFactory
import scala.concurrent.{ Await, Future }
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import houseprices.search.model._
import houseprices.search.model.SearchResultUnmarshaller._

trait SearchClient {
  def search(query: Query): SearchResult
}

trait HttpRequestFactory {
  def request(req: HttpRequest): Future[HttpResponse]
}

trait AkkaHttpRequestFactory extends HttpRequestFactory {
  implicit val system: ActorSystem
  implicit val mat: ActorMaterializer
  val http = Http(system)
  def request(req: HttpRequest) = http.singleRequest(req)
}

class HttpSearchClient(implicit val system: ActorSystem) extends SearchClient {
  this: HttpRequestFactory =>
  import QueryToElasticsearch._
  import SearchResultUnmarshaller._

  private val searchUrl = "http://localhost:9200/pricepaid/uk/_search"
  private val log = LoggerFactory.getLogger(getClass)
  implicit val mat = ActorMaterializer()
  def search(query: Query) = {
    val searchJson = query.toElasticsearch
    log.debug("json: {}", searchJson)
    val response = Await.result(request(HttpRequest(HttpMethods.GET, uri = searchUrl, entity = searchJson)), 3.seconds)
    log.debug("entity: {}", response)
    SearchResult(0, List.empty)
  }
}

object HttpSearchClient {
  def apply(implicit system: ActorSystem) = new HttpSearchClient with AkkaHttpRequestFactory
}
