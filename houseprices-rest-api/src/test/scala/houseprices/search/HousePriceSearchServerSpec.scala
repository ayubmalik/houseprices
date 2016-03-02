package houseprices.search

import org.scalatest.Matchers
import org.scalatest.WordSpec

import akka.actor.ActorSystem

class HousePriceSearchServerSpec extends WordSpec with Matchers {

  implicit val system = ActorSystem("test")
  implicit val ec = system.dispatcher

  "HousePriceSearchServer" when {

    "searching" should {

      "return search result" in {

      }

    }
  }
}
