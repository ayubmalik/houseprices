package houseprices.admin.api

import scala.concurrent.Future
import scala.concurrent.Promise
import org.scalatest.Matchers
import org.scalatest.WordSpec
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.HttpMethod
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.MediaTypes._
import akka.http.scaladsl.model.RequestEntity
import akka.http.scaladsl.model.Uri.apply
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.HttpMethods
import akka.http.scaladsl.server.UnsupportedRequestContentTypeRejection
import akka.event.Logging

class AdminServerSpec extends WordSpec
    with Matchers with ScalatestRouteTest with AdminService with AdminJsonProtocols {

  import spray.json._
  import DataDownloadMessages._

  val logger = null

  def client = new HttpClient with HttpRequestService {
    def makeRequest(method: HttpMethod, uri: String) = Promise[String].future
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
        val post = Post("/admin/datadownloads", HttpEntity(`application/json`, """{"url": "hello url", "fileName":"file name"}"""))
        post ~> routes ~> check {
          status.intValue shouldEqual 202
        }
      }

      "reject POST for non JSON body" in {
        val body = Marshal("data file url").to[RequestEntity]
        val post = Post("/admin/datadownloads", "not json")
        post ~> routes ~> check {
          rejection shouldEqual UnsupportedRequestContentTypeRejection(Set(`application/json`))
          //handled shouldEqual false
        }
      }
    }
  }

}