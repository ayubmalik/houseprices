package houseprices.restapi

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.ToResponseMarshallable._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.RouteResult.route2HandlerFlow
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.HttpRequest
import akka.stream.scaladsl.{ Flow, Source, Sink }
import akka.stream.ActorMaterializer
import scala.concurrent.Future
import java.io.IOException
import akka.http.scaladsl.client.RequestBuilding
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.http.scaladsl.model.StatusCodes
import scala.concurrent.ExecutionContext.Implicits.global
import akka.http.scaladsl.marshalling.ToResponseMarshallable

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
          complete {
            searchByPostcode("Chorlton").map[ToResponseMarshallable](r => r)
          }
        }
      }

  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
  println(s"Server online at http://localhost:8080/")

}