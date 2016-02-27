package houseprices.search

import org.scalatest.WordSpec
import org.scalatest.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

class PostcodeMatcherSpec extends WordSpec with Matchers with TableDrivenPropertyChecks {

  val postcodes = Table(("postcode"),
    ("AB24 5HT"), ("B4 7ES"),   ("PO31 8LD"), ("HU14 3JD"),
    ("DT3 6FE"),  ("BB94 0AA"), ("M24 1WR"),  ("OX39 4TX"),
    ("ML3 9LQ"),  ("HP19 8FA"), ("NN3 3XG"),  ("NN33XG"),
    ("S75 4NA"),  ("S754NA"),   ("E1 2QL"),   ("E12QL"),
    ("E14 8AG"),  ("ZE2 9JJ"),  ("W1J 5LD"),  ("BR6 9HQ"),
    ("AA0A 0AA"), ("aa0a 0aa"), ("AA0A0AA"),  ("aa0a0aa")
  )

  "PostcodeMatcher" should {
    "match valid postcode" in {
      forAll(postcodes) { postcode =>
        PostcodeMatcher(postcode).matches shouldBe true
      }
    }
  }
}
