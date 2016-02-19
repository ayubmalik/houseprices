package houseprices.admin.api

import scala.concurrent.Future
import akka.stream.ActorMaterializer
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.HttpMethods
import scala.concurrent.ExecutionContext
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.http.scaladsl.model.HttpMethod

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
  implicit def ec: ExecutionContext

  lazy val http = Http(system)

  override def makeRequest(method: HttpMethod, uri: String): Future[String] = {
    val req = HttpRequest(method, uri)
    val res = http.singleRequest(req)
    res.flatMap { r => println(r); Unmarshal(r.entity).to[String] }
  }

  def shutdown() = {
    http.shutdownAllConnectionPools().onComplete { _ =>
      println("http connections shutdown")
    }
  }
}
