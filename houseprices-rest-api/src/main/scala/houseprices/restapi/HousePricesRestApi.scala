package houseprices.restapi

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.ToResponseMarshallable.apply
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.RouteResult.route2HandlerFlow
import akka.stream.ActorMaterializer
import scala.collection.Searching.Found
import akka.http.scaladsl.model.StatusCodes

object HousePricesRestApi extends App {

  implicit val system = ActorSystem("houseprices-system")
  implicit val materializer = ActorMaterializer()
  val route =
    path("") {
      redirect("/houseprices/wa3%206xf", StatusCodes.Found)
    } ~
      path("houseprices" / ".*".r) { postcode =>
        get {
          complete {
            s"Searching for postcode ${postcode.toUpperCase} "
          }
        }
      }

  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
  println(s"Server online at http://localhost:8080/")

}