package houseprices.search

class PostcodeFullMatcher(val postcode: String) {
  private val ukPostcode = "[A-Z][A-Z]?[0-9][0-9]?[A-Z]?\\s?[0-9][A-Z][A-Z]".r.pattern

  def matches(): Boolean = {
    ukPostcode.matcher(postcode.toUpperCase).matches
  }
}

object PostcodeFullMatcher {
  def apply(postcode: String) = new PostcodeFullMatcher(postcode)
}


class PostcodeOutwardCodeMatcher(val outwardCode: String) {
  private val ukOutwardCode = "[A-Z][A-Z]?[0-9][0-9]?[A-Z]?".r.pattern

  def matches(): Boolean = {
    ukOutwardCode.matcher(outwardCode.toUpperCase).matches
  }
}

object PostcodeOutwardCodeMatcher {
  def apply(outwardCode: String) = new PostcodeOutwardCodeMatcher(outwardCode)
}
