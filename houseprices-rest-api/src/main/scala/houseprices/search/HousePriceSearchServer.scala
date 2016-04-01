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
import houseprices.search.model._
import houseprices.search.model.SearchResultJsonProtocol._
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import scala.concurrent.ExecutionContext
import spray.json.DefaultJsonProtocol
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport

trait SearchJsonProtocol extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val format2 = jsonFormat3(PricePaidData)
  implicit val format1 = jsonFormat2(SearchResult)
}

trait HousePriceSearchService extends SearchJsonProtocol {
  def searchClient: SearchClient
  implicit val ec: ExecutionContext

  val routes =
    path("search" / ".*".r) { text =>
      get {
        complete {
          searchClient.search(Query(text))
        }
      }
    }
}

class HousePriceSearchServer(val searchClient: SearchClient)(implicit val system: ActorSystem, implicit val materializer: ActorMaterializer) extends HousePriceSearchService {

  val log = Logging(system, getClass)
  implicit val ec = system.dispatcher
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