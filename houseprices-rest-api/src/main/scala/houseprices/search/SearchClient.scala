package houseprices.search

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import org.slf4j.LoggerFactory
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpEntity.apply
import akka.http.scaladsl.model.HttpMethods
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.Uri.apply
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import houseprices.search.model._
import houseprices.search.model.SearchResult


trait SearchClient {
  def search(query: Query): Future[SearchResult]
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

  implicit val mat = ActorMaterializer()

  private val searchUrl = "http://localhost:9200/pricepaid/uk/_search"
  private val log = LoggerFactory.getLogger(getClass)

  def search(query: Query) = {
    val searchJson = query.toElasticsearch
    log.debug("json: {}", searchJson)
    request(HttpRequest(HttpMethods.GET, uri = searchUrl, entity = searchJson)).flatMap { r =>
      Unmarshal(r.entity).to[SearchResult]
    }
  }
}

object HttpSearchClient {
  def apply(implicit system: ActorSystem) = new HttpSearchClient with AkkaHttpRequestFactory
}
