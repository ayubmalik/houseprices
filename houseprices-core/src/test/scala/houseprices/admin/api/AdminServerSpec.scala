package houseprices.admin.api

import org.scalatest.Matchers
import org.scalatest.WordSpec
import akka.http.scaladsl.testkit.ScalatestRouteTest

class AdminServerSpec extends WordSpec with Matchers with ScalatestRouteTest {

  "Admin Server" when {

    "url is /admin/datadownloads" should {

      "return 200 for GET" in {

      }
    }
  }

}