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
import akka.http.scaladsl.marshalling.Marshal

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

      "return active count for GET" in {

        val getRequest = HttpRequest(HttpMethods.GET, uri = "/admin/datadownloads")
        getRequest ~> routes ~> check {
          status.isSuccess() shouldEqual true
          val active = ActiveDownloads(0, List.empty)
          responseEntity shouldEqual HttpEntity(`application/json`, active.toJson.prettyPrint)
        }
      }

      "return 202 for POST" in {

        val post = Post("/admin/datadownloads", "some url")
        post ~> routes ~> check {
          status.intValue shouldEqual 202
        }
      }

      "return 400 for POST with missing url" in {
        val body = Marshal("data file url").to[RequestEntity]
        val post = Post("/admin/datadownloads")
        post ~> routes ~> check {
          status.intValue shouldEqual 400
        }
      }
    }
  }

}