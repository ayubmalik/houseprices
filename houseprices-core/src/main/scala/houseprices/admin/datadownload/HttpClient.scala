package houseprices.admin.datadownload

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethod
import akka.http.scaladsl.model.HttpMethods
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.Uri.apply
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer

trait HttpClient {
  this: HttpRequestService =>
  def get(uri: String) = makeRequest(HttpMethods.GET, uri)
}

trait HttpRequestService {
  def makeRequest(method: HttpMethod, uri: String): Future[String]
}

trait AkkaHttpRequestService extends HttpRequestService {

  implicit def system: ActorSystem
  implicit def materializer: ActorMaterializer
  implicit val ec: ExecutionContext

  lazy val http = Http(system)
  val oneGigabyte = 1073741824

  override def makeRequest(method: HttpMethod, uri: String): Future[String] = {
    val req = HttpRequest(method, uri)
    val res = http.singleRequest(req)
    res.flatMap { r => Unmarshal(r.entity.withSizeLimit(oneGigabyte)).to[String] }
  }

  def shutdown() = http.shutdownAllConnectionPools
}

object AkkaHttpClient {
  def apply(sys: ActorSystem) = {
    new HttpClient with AkkaHttpRequestService {
      override implicit val system = sys
      override implicit val materializer = ActorMaterializer.create(system)
      override implicit val ec: ExecutionContext = system.dispatcher
    }
  }
}

object HttpClientApp extends App {

  val system = ActorSystem("Me")
  implicit val ec = system.dispatcher

  val client = AkkaHttpClient(system)
  client.get("http://textfiles.com/art/simpsons.txt").onSuccess {
    case _@ t =>
      println(t)
      client.shutdown.onSuccess { case _ => system.terminate }
  }

}