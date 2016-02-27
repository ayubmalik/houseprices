package houseprices.search

class PostcodeMatcher(val postcode: String) {
  private val ukPostcode = "[A-Z][A-Z]?[0-9][0-9]?[A-Z]?\\s?[0-9][A-Z][A-Z]".r.pattern

  def matches(): Boolean = {
    ukPostcode.matcher(postcode.toUpperCase).matches
  }
}

object PostcodeMatcher {
  def apply(postcode: String) = new PostcodeMatcher(postcode)
}
