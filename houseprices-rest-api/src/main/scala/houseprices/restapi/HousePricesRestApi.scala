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

object HousePricesRestApi extends App {

  implicit val system = ActorSystem("houseprices-system")
  implicit val materializer = ActorMaterializer()

  lazy val elasticSearchFlow: Flow[HttpRequest, HttpResponse, Any] = Http().outgoingConnection("localhost", 9200)

  def esHousePriceSearch(request: HttpRequest): Future[HttpResponse] =
    Source.single(request).via(elasticSearchFlow).runWith(Sink.head)

  def searchByPostcode(postcode: String): Future[String] = {
    esHousePriceSearch(RequestBuilding.Get(s"/pricepaid/uk/_search?q=$postcode")).flatMap { response =>
      response.status match {
        case OK => Unmarshal(response.entity).to[String]
        case BadRequest => Future.successful("whoops bad request")
        case _ => Unmarshal(response.entity).to[String].flatMap { entity =>
          val error = s"FAIL - ${response.status}"
          Future.failed(new IOException(error))
        }
      }
    }
  }

  val route =
    path("") {
      redirect("/houseprices/wa3%206xf", StatusCodes.Found)
    } ~
      path("houseprices" / ".*".r) { postcode =>
        get {
          complete(searchByPostcode("Chorlton").map[HttpResponse](body => HttpResponse(entity = HttpEntity(ContentType(MediaTypes.`application/json`), body))))
        }
      }

  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
  println(s"Server online at http://localhost:8080/")

}