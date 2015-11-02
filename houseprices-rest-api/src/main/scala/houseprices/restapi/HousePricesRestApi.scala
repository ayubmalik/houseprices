package houseprices.restapi

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.ToResponseMarshallable.apply
import akka.http.scaladsl.server.Directive.addByNameNullaryApply
import akka.http.scaladsl.server.Directives.complete
import akka.http.scaladsl.server.Directives.get
import akka.http.scaladsl.server.Directives.path
import akka.http.scaladsl.server.Directives.segmentStringToPathMatcher
import akka.http.scaladsl.server.RouteResult.route2HandlerFlow
import akka.stream.ActorMaterializer
import scala.io.StdIn

object HousePricesRestApi extends App {

  implicit val system = ActorSystem("houseprices-system")
  implicit val materializer = ActorMaterializer()
  var counter = 0
  val route =
    path("hello") {
      get {
        complete {
          counter = counter + 1
          "Say hello to akka-http " + counter
        }
      }
    }
  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
  println(s"Server online at http://localhost:8080/")

}