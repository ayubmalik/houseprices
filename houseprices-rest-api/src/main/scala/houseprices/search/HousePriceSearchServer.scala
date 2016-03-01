package houseprices.restapi

import java.io.IOException
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding
import akka.http.scaladsl.marshalling.ToResponseMarshallable.apply
import akka.http.scaladsl.model.ContentType
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.MediaTypes
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.StatusCodes.BadRequest
import akka.http.scaladsl.model.StatusCodes.OK
import akka.http.scaladsl.model.Uri.apply
import akka.http.scaladsl.server.Directive.addByNameNullaryApply
import akka.http.scaladsl.server.Directive.addDirectiveApply
import akka.http.scaladsl.server.Directives.complete
import akka.http.scaladsl.server.Directives.enhanceRouteWithConcatenation
import akka.http.scaladsl.server.Directives.get
import akka.http.scaladsl.server.Directives.path
import akka.http.scaladsl.server.Directives.redirect
import akka.http.scaladsl.server.Directives.regex2PathMatcher
import akka.http.scaladsl.server.Directives.segmentStringToPathMatcher
import akka.http.scaladsl.server.RouteResult.route2HandlerFlow
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Flow
import akka.stream.scaladsl.Sink
import akka.stream.scaladsl.Source
import houseprices.search.AkkaHttpRequestFactory
import houseprices.search.HttpSearchClient
import houseprices.search.SearchClient
import akka.event.Logging

trait HousePriceSearchService {

  def searchClient: SearchClient

  val routes =
    path("/search") {
      get {
        complete("todo")
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
  implicit val system = ActorSystem("housepricesAdminSystem")
  implicit val materializer = ActorMaterializer()
  val searchClient = HttpSearchClient(system)
  new HousePriceSearchServer(searchClient).startServer("localhost", 8080)
}