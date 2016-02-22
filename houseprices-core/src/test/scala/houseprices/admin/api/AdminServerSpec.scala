package houseprices.admin.api

import org.scalatest.Matchers
import org.scalatest.WordSpec
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.MediaTypes._
import akka.http.scaladsl.model.Uri.apply
import akka.http.scaladsl.testkit.ScalatestRouteTest
import spray.json.DefaultJsonProtocol
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import scala.concurrent.Future
import scala.concurrent.Promise

class AdminServerSpec extends WordSpec
    with Matchers with ScalatestRouteTest with AdminService with AdminJsonProtocols {

  import spray.json._
  import DataDownloadMessages._

  val promise = Promise[String]

  def client = new HttpClient with HttpRequestService {
    def makeRequest(method: HttpMethod, uri: String) = promise.future
  }

  "Admin Server" when {

    "url is /admin/datadownloads" should {

      "return active workers for GET" in {

        val getRequest = HttpRequest(HttpMethods.GET, uri = "/admin/datadownloads")
        getRequest ~> routes ~> check {
          status.isSuccess() shouldEqual true
          val active = ActiveWorkers(0)
          responseEntity shouldEqual HttpEntity(`application/json`, active.toJson.prettyPrint)
        }

      }
    }
  }

}