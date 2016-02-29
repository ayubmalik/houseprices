package houseprices.search.model

import java.time.LocalDate

case class SearchResult(count: Int, priceData: List[PricePaidData])

case class PricePaidData(price: Int, date:LocalDate)