package houseprices.admin.api

import scala.concurrent.Future
import akka.stream.ActorMaterializer
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.HttpMethods
import scala.concurrent.ExecutionContext
import akka.http.scaladsl.unmarshalling.Unmarshal

trait HttpRequestService {
  def get(uri: String): Future[String]
}

trait AkkaHttpRequestService extends HttpRequestService {

  implicit def system: ActorSystem
  implicit def materializer: ActorMaterializer
  implicit def ec: ExecutionContext

  lazy val http = Http(system)

  def makeRequest(uri: String): Future[String] = {
    println(uri)
    val req = HttpRequest(HttpMethods.GET, uri)
    val resp = http.singleRequest(req)
    resp.flatMap { r => println(r); Unmarshal(r.entity).to[String] }
  }

  def shutdown() = {
    http.shutdownAllConnectionPools().onComplete { _ =>
      println("http connections shutdown")
    }
  }
}