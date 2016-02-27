package houseprices.search

import org.scalatest.WordSpec
import org.scalatest.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

class PostcodeMatchersSpec extends WordSpec with Matchers with TableDrivenPropertyChecks {

  val postcodes = Table(("postcode"),
    ("AB24 5HT"), ("B4 7ES"),   ("PO31 8LD"), ("HU14 3JD"),
    ("DT3 6FE"),  ("BB94 0AA"), ("M24 1WR"),  ("OX39 4TX"),
    ("ML3 9LQ"),  ("HP19 8FA"), ("NN3 3XG"),  ("NN33XG"),
    ("S75 4NA"),  ("S754NA"),   ("E1 2QL"),   ("E12QL"),
    ("E14 8AG"),  ("ZE2 9JJ"),  ("W1J 5LD"),  ("BR6 9HQ"),
    ("AA0A 0AA"), ("aa0a 0aa"), ("AA0A0AA"),  ("aa0a0aa")
  )

  val outwardCodes = Table(("outward code"),
    ("AB24"), ("B4"),   ("PO31"), ("HU14"),
    ("DT3"),  ("BB94"), ("M24"),  ("OX39"),
    ("ML3"),  ("HP19"), ("NN3"),  ("S75"),
    ("E1"),   ("E14"),  ("ZE2"),  ("W1J"),
    ("BR6"),  ("AA0A"), ("aa0a")
  )

  val others = Table(("non postcode"),
    ("I am"), ("Manchester"), ("Chorlton"), ("Yorks"),
    ("UK"),  ("123"), ("my"),  ("england"),
    ("London"),  ("notts"), ("a b c"),  ("M 1")
  )

  "PostcodeFullMatcher" should {
    "match valid postcode" in {
      forAll(postcodes) { postcode =>
        PostcodeFullMatcher(postcode).matches shouldBe true
      }
    }
  }

  "PostcodeFullMatcher" should {
    "not match non postcode" in {
      forAll(others) { other =>
        PostcodeFullMatcher(other).matches shouldBe false
      }
    }
  }

  "PostcodeOutwardCodeMatcher" should {
    "match valid outward code" in {
      forAll(outwardCodes) { outwardCode =>
        PostcodeOutwardCodeMatcher(outwardCode).matches shouldBe true
      }
    }
  }

  "PostcodeOutwardCodeMatcher" should {
    "not match non outward code" in {
      forAll(others) { outwardCode =>
        PostcodeOutwardCodeMatcher(outwardCode).matches shouldBe false
      }
    }
  }
}
