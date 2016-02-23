package houseprices.admin.api

import scala.concurrent.duration.DurationInt

import com.typesafe.config.ConfigFactory

import DataDownloadMessages.ActiveWorkers
import DataDownloadMessages.ShowActive
import akka.actor.ActorSystem
import akka.actor.Props
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.marshalling.ToResponseMarshallable.apply
import akka.http.scaladsl.server.Directive.addByNameNullaryApply
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Directives.get
import akka.http.scaladsl.server.Directives.path
import akka.http.scaladsl.server.Directives.pathPrefix
import akka.http.scaladsl.server.Directives.post
import akka.http.scaladsl.server.Directives.segmentStringToPathMatcher
import akka.http.scaladsl.server.RouteResult.route2HandlerFlow
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.util.Timeout
import spray.json.DefaultJsonProtocol

trait AdminJsonProtocols extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val activeFormat = jsonFormat1(ActiveWorkers)
}

trait AdminService extends AdminJsonProtocols {

  implicit val system: ActorSystem
  implicit val timeout = Timeout(5 seconds)

  val downloader = system.actorOf(Props(classOf[DataDownloader], "/tmp/houseprices"))

  def client: HttpClient

  val routes =
    pathPrefix("admin") {
      path("datadownloads") {
        get {
          complete {
            (downloader ? ShowActive).mapTo[ActiveWorkers]
          }
        } ~
          post {
            entity(as[String]) { dataFileUrl =>
              complete {
                "post placeholder: " + dataFileUrl
              }
            }

          }
      }
    }
}

class AdminServer(implicit val system: ActorSystem, implicit val materializer: ActorMaterializer) extends AdminService {

  def startServer(interface: String, port: Int) = {
    Http().bindAndHandle(routes, interface, port)
    this
  }

  def client = AkkaHttpClient(system)
}

object AdminServer extends App {
  implicit val system = ActorSystem("housepricesAdminSystem")
  implicit val materializer = ActorMaterializer()
  val config = ConfigFactory.load()
  val logger = Logging(system, getClass)

  val (interface, port) = (config.getString("akka.http.interface"), config.getInt("akka.http.port"))
  val server = new AdminServer().startServer(interface, port)
  logger.info(s"Server ready at {}:{}", interface, port)
}