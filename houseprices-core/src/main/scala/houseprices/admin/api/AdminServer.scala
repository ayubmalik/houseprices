package houseprices.admin.api

import scala.concurrent.duration.DurationInt
import com.typesafe.config.ConfigFactory
import DataDownloadMessages.ActiveDownloads
import DataDownloadMessages._
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
import scala.concurrent.Future
import akka.http.scaladsl.model.HttpResponse
import akka.event.LoggingAdapter

trait AdminJsonProtocols extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val format1 = jsonFormat3(ActiveDownload)
  implicit val format2 = jsonFormat2(ActiveDownloads)
  implicit val format3 = jsonFormat2(Download)

}

trait AdminService extends AdminJsonProtocols {

  implicit val system: ActorSystem
  implicit val timeout = Timeout(5 seconds)

  import DataDownloadMessages._

  def client: HttpClient
  val logger: LoggingAdapter

  val downloader = system.actorOf(Props(classOf[DataDownloadManager], "/tmp/houseprices", client))

  val routes =
    pathPrefix("admin") {
      path("datadownloads") {
        get {
          complete {
            (downloader ? ShowActive).mapTo[ActiveDownloads]
          }
        } ~
          post {
            entity(as[Download]) { download =>
              if (download.url.isEmpty) complete(HttpResponse(status = 400))
              else
                println("download: " + download)
              complete(HttpResponse(status = 202))
            }
          }
      }
    }
}

class AdminServer(implicit val system: ActorSystem, implicit val materializer: ActorMaterializer) extends AdminService {

  def startServer(interface: String, port: Int) = {
    Http().bindAndHandle(routes, interface, port)
    logger.info("Admin server ready at {}:{}", interface, port)
    this
  }

  def client = AkkaHttpClient(system)
  val logger = Logging(system, getClass)
}

object AdminServer extends App {
  implicit val system = ActorSystem("housepricesAdminSystem")
  implicit val materializer = ActorMaterializer()
  val config = ConfigFactory.load()

  val (interface, port) = (config.getString("akka.http.interface"), config.getInt("akka.http.port"))
  val server = new AdminServer().startServer(interface, port)
}