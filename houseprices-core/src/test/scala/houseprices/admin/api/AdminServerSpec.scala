package houseprices.admin.api

import scala.concurrent.Promise

import org.scalatest.Matchers
import org.scalatest.WordSpec

import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.ContentType.apply
import akka.http.scaladsl.model.ContentTypeRange.apply
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.HttpMethod
import akka.http.scaladsl.model.HttpMethods
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.RequestEntity
import akka.http.scaladsl.model.MediaTypes._
import akka.http.scaladsl.server.UnsupportedRequestContentTypeRejection
import akka.http.scaladsl.testkit.ScalatestRouteTest
import houseprices.admin.datadownload.DataDownloadMessages.ActiveDownloads
import houseprices.admin.datadownload.HttpClient
import houseprices.admin.datadownload.HttpRequestService
import spray.json.pimpAny

class AdminServerSpec extends WordSpec
    with Matchers with ScalatestRouteTest with AdminService with AdminJsonProtocols {

  import spray.json._

  val log = null

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

    "when url is /admin/dataimports/" should {
      "accept POST for csv file to import" in {

        val post = Post("/admin/dataimports", "csv file")
        post ~> routes ~> check {
         status.intValue shouldEqual 202
        }
      }
    }
  }

}