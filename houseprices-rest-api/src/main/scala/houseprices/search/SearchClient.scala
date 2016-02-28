package houseprices.search

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods,HttpRequest, HttpResponse}
import org.slf4j.LoggerFactory
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

case class Query(query: String)

trait SearchClient {
  def search(query: Query): SearchResult
}

trait HttpRequestFactory {
  def request(req: HttpRequest): Future[HttpResponse]
}

trait AkkaHttpRequestFactory extends HttpRequestFactory {
  implicit val system: ActorSystem
  implicit val materializer = ActorMaterializer()
  val http = Http(system)
  def request(req: HttpRequest) = http.singleRequest(req)
}

class HttpSearchClient(implicit val system: ActorSystem) extends SearchClient {
  this: HttpRequestFactory =>
  val log = LoggerFactory.getLogger(getClass)
  def search(query: Query) = {
    val response = Await.result(request(HttpRequest(HttpMethods.GET,
      uri = "http://localhost:9200/pricepaid/uk/_search?q=")), 3.seconds)
    log.info("response:" + response)
    SearchResult(0) // TODO: unmarshalling etc
  }
}

object HttpSearchClient {
  def apply(implicit system: ActorSystem) = new HttpSearchClient with AkkaHttpRequestFactory
}
