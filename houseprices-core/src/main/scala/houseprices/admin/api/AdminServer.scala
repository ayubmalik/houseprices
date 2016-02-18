package houseprices.admin.api

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import akka.http.scaladsl.Http
import akka.event.Logging
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.xml.ScalaXmlSupport._
import akka.stream.ActorMaterializer

case class ActiveDownloads(count: Int)

trait AdminService {

  val routes =
    pathPrefix("admin") {
      path("datadownloads") {
        get {
          complete {
            <h1>Admin placeholder</h1>
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
}

object AdminServer extends App with AdminService {
  implicit val system = ActorSystem("housepricesAdminSystem")
  implicit val materializer = ActorMaterializer()
  val config = ConfigFactory.load()
  val logger = Logging(system, getClass)

  val (interface, port) = (config.getString("akka.http.interface"), config.getInt("akka.http.port"))
  val server = new AdminServer().startServer(interface, port)
  logger.info(s"Server ready at {}:{}", interface, port)
}