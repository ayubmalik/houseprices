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

object AdminJsonProtocols extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val activeFormat = jsonFormat1(ActiveDownloads)
}

class AdminServerSpec extends WordSpec
    with Matchers with ScalatestRouteTest with AdminService {

  import AdminJsonProtocols._

  "Admin Server" when {

    "url is /admin/datadownloads" should {

      "return current downloads for GET" in {

        val getRequest = HttpRequest(HttpMethods.GET, uri = "/admin/datadownloads")
        getRequest ~> routes ~> check {
          status.isSuccess() shouldEqual true
          val active = ActiveDownloads(0)
          responseEntity shouldEqual HttpEntity(`application/json`, "meh")
        }

      }
    }
  }

}