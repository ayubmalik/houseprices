package houseprices.admin.api

import org.scalatest.Matchers
import org.scalatest.WordSpec

import akka.http.scaladsl.model.HttpMethods
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.Uri.apply
import akka.http.scaladsl.testkit.ScalatestRouteTest

class AdminServerSpec extends WordSpec
    with Matchers with ScalatestRouteTest with AdminService {

  "Admin Server" when {

    "url is /admin/datadownloads" should {

      "return 200 for GET" in {

        val getRequest = HttpRequest(HttpMethods.GET, uri = "/admin/datadownloads")
        getRequest ~> routes ~> check {
          status.isSuccess() shouldEqual true
        }

      }
    }
  }

}