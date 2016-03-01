package houseprices.search

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.ToResponseMarshallable.apply
import akka.http.scaladsl.server.Directive.addByNameNullaryApply
import akka.http.scaladsl.server.Directives.complete
import akka.http.scaladsl.server.Directives.get
import akka.http.scaladsl.server.Directives.path
import akka.http.scaladsl.server.Directives.segmentStringToPathMatcher
import akka.http.scaladsl.server.RouteResult.route2HandlerFlow
import akka.stream.ActorMaterializer
import houseprices.search.model.Query
import akka.http.scaladsl.marshalling.ToResponseMarshallable

trait HousePriceSearchService {

  def searchClient: SearchClient

  val routes =
    path("search" / ".*".r) { text =>
      get {
        complete {
           ToResponseMarshallable("hello")
        }
      }
    }
}

class HousePriceSearchServer(val searchClient: SearchClient)(implicit val system: ActorSystem, implicit val materializer: ActorMaterializer) extends HousePriceSearchService {

  val log = Logging(system, getClass)

  def startServer(interface: String, port: Int) = {
    Http().bindAndHandle(routes, interface, port)
    log.info("Search server ready at {}:{}", interface, port)
    this
  }
}

object DevHousePriceSearchServer extends App {
  implicit val system = ActorSystem("housePriceSearchServer")
  implicit val materializer = ActorMaterializer()
  val searchClient = HttpSearchClient(system)
  new HousePriceSearchServer(searchClient).startServer("localhost", 8080)
}